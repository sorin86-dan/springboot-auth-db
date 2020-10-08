pipeline {

    agent any
    tools {
        maven 'Maven 3.6.0'
    }
    stages {
        stage('Retrieve code from GitHub') {
            steps {
                git 'https://github.com/sorin86-dan/springboot-auth-db'
            }
        }

        stage('Build and run springboot-auth, springboot-db and Redis Docker instances') {
            steps {
                dir ('src/test/resources') {
                  sh 'sudo docker-compose up -d'
                }
            }
        }

        stage('Run automated integration tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Stop and remove Docker instances') {
            steps {
                sh 'docker stop $(docker ps -a -q)'
                sh 'docker system prune --all'
                sh 'y'
            }
        }
    }
}
