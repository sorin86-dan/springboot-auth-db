pipeline {

    agent any
    tools {
        maven 'Maven 3.6.0'
        jdk 'jdk11'
    }
    stages {
        stage('Retrieve code from GitHub') {
            steps {
                git 'https://github.com/sorin86-dan/springboot-auth-db'
            }
        }

        stage('Build and run Docker instances') {
            steps {
                dir ('src/test/resources') {
                  sh 'sudo docker-compose up -d'
                }
            }
        }

        stage('Run automated integration tests') {
            steps {
                sh 'sudo docker network connect resources_grid jenkins-container'
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            sh 'sudo docker stop redis-db db-ms auth-ms'
            sh 'sudo docker system prune -a -f'
        }
    }
}
