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
                        bat 'docker build -t selenium-tests .'
                        bat 'docker run --rm selenium-tests'
                    } catch (Exception e) {
                        echo "Docker command failed: ${e.message}"
                        currentBuild.result = 'FAILURE'
                        error("Docker command failed")
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