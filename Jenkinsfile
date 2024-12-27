pipeline {
    agent any
    
    environment {
        DOCKER_HOST = 'npipe:////./pipe/docker_engine'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/HassanBaigDEV/test-app.git'
            }
        }
        
        stage('Docker Status') {
            steps {
                bat 'docker info'
            }
        }
        
        stage('Test') {
            steps {
                script {
                    try {
                        // Create network if it doesn't exist
                        bat 'docker network create selenium-test-network || true'
                        
                        // Start MySQL if not running
                        bat 'docker run --name mysql-db --network selenium-test-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=selenium_test_db -d mysql:8.0 || true'
                        
                        // Wait for MySQL to be ready
                        bat 'timeout /t 30'
                        
                        // Build and run tests
                        bat 'docker build -t selenium-tests .'
                        bat 'docker run --rm --network selenium-test-network selenium-tests'
                    } catch (Exception e) {
                        echo "Docker command failed: ${e.message}"
                        currentBuild.result = 'FAILURE'
                        error("Docker command failed")
                    } finally {
                        // Cleanup
                        bat 'docker stop mysql-db || true'
                        bat 'docker rm mysql-db || true'
                        bat 'docker network rm selenium-test-network || true'
                    }
                }
            }
        }
    }
    
    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        failure {
            echo 'The Pipeline failed :('
        }
    }
} 