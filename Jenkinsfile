pipeline {
    agent any

    tools {
        jdk   "jdk17"
        maven "maven"
    }

    environment {
        APP_PORT = "9090"   // tránh đụng Jenkins 8080
        BASE_URL = "http://localhost:9090"
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
                // Start Spring Boot + chờ health bằng PowerShell để tránh lỗi %i / errorlevel
                bat """
                    echo Starting Spring Boot on port %APP_PORT%...

                    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
                      "$ErrorActionPreference='Stop';" ^
                      "$mvn = 'mvn';" ^
                      "$log = 'app.log';" ^
                      "$args = @('spring-boot:run','-Dspring-boot.run.arguments=--server.port=%APP_PORT%');" ^
                      "$p = Start-Process $mvn -ArgumentList $args -RedirectStandardOutput $log -RedirectStandardError $log -WindowStyle Hidden -PassThru;" ^
                      "$p.Id | Out-File -FilePath app.pid -Encoding ascii;" ^
                      "Write-Host ('Spring Boot PID: ' + $p.Id);" ^
                      "$deadline = (Get-Date).AddMinutes(2);" ^
                      "while ((Get-Date) -lt $deadline) {" ^
                      "  try { iwr -UseBasicParsing 'http://localhost:%APP_PORT%' -TimeoutSec 3 | Out-Null; Write-Host 'App is UP'; exit 0 }" ^
                      "  catch { Start-Sleep -Seconds 2 }" ^
                      "}" ^
                      "Write-Error 'App failed to start in time';" ^
                      "if (Test-Path app.log) { Get-Content -Tail 100 app.log }; exit 1"
                """
            }
        }

        stage('Run UI Tests') {
            steps {
                // Nếu test đọc BASE_URL từ System env, truyền vào maven như sau (giữ nguyên nếu không cần):
                bat 'mvn test -DbaseUrl=%BASE_URL%'
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
            echo "Stopping Spring Boot safely..."
            // Dùng PID đã lưu để stop đúng process, không bao giờ kill Jenkins
            bat """
                if exist app.pid (
                  for /f %%p in (app.pid) do powershell -NoProfile -ExecutionPolicy Bypass -Command "Stop-Process -Id %%p -Force -ErrorAction SilentlyContinue"
                  del /f /q app.pid
                ) else (
                  echo No app.pid found, trying window-title fallback...
                  for /f "tokens=2" %%p in ('tasklist /v ^| findstr /i "vegana-app"') do taskkill /F /PID %%p >nul 2>&1
                )
                echo Cleanup complete.
            """
        }
    }
}
