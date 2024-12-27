pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/HassanBaigDEV/test-app.git'
            }
        }
        
        stage('Test') {
            steps {
                script {
                    docker.build('selenium-tests').inside {
                        sh 'mvn test'
                    }
                }
            }
        }
    }
    
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
} 