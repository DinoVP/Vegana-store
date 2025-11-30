pipeline {
    agent any

    tools {
        jdk "jdk17"
        maven "maven"
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
                bat '''
                    echo Starting Spring Boot...
                    start "" /B mvn spring-boot:run > app.log 2>&1

                    echo Waiting for Spring Boot to be ready...

                    REM === Loop 40 lần để check http://localhost:9090 ===
                    for /l %%x in (1,1,40) do (
                        curl -s http://localhost:9090 >nul
                        if %errorlevel%==0 (
                            echo Spring Boot is UP!
                            goto :done
                        )
                        echo Waiting for app (%%x/40)...
                        ping -n 2 127.0.0.1 >nul
                    )
                    :done
                '''
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
                archiveArtifacts artifacts: 'app.log', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            bat '''
                echo Stopping Spring Boot if running...
                taskkill /F /IM java.exe >nul 2>&1
                echo Done.
            '''
        }
    }
}
