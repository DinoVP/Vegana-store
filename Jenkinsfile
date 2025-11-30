pipeline {
    agent any

    tools {
        jdk "jdk17"
        maven "maven"
    }

    environment {
        APP_PORT = "9090"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build App') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Start App') {
            steps {
                bat """
                    echo Starting Spring Boot on port %APP_PORT%...
                    start "vegana-app" /B mvn spring-boot:run > app.log 2>&1

                    echo Checking if Spring Boot is ready...

                    REM === Loop 40 lần check http://localhost:%APP_PORT% ===
                    for /l %%i in (1,1,40) do (
                        curl -s http://localhost:%APP_PORT% >nul
                        if %%errorlevel%%==0 (
                            echo Spring Boot is UP!
                            goto ready
                        )
                        echo Waiting (%%i/40)...
                        ping -n 2 127.0.0.1 >nul
                    )

                    echo ❌ Spring Boot FAILED to start!
                    exit /b 1

                    :ready
                    echo Spring Boot is ready.
                """
            }
        }

        stage('Run UI Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo "Stopping Spring Boot safely..."

            bat """
                echo Looking for Spring Boot process (vegana-app)...

                REM Chỉ kill đúng process Spring Boot, không kill Jenkins
                for /f "tokens=2" %%p in ('tasklist /v ^| findstr /i "vegana-app"') do (
                    echo Killing PID %%p
                    taskkill /F /PID %%p >nul 2>&1
                )

                echo Cleanup complete.
            """
        }
    }
}
