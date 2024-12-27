pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/HassanBaigDEV/test-app.git'
            }
        }
        
        stage('Test') {
            steps {
                script {
                    bat 'docker build -t selenium-tests .'
                    bat 'docker run selenium-tests'
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