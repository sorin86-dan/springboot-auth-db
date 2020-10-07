pipeline {

    agent any

    stages {
        stage('Retrieve code from GitHub') {
            steps {
                git 'https://github.com/sorin86-dan/springboot-auth-db'
            }
        }

        stage('Build and run springboot-auth, springboot-db and Redis Docker instances') {
            steps {
                sh "cd sprinboot-auth-db/src/test/resources"
                sh "docker-compose up"
            }
        }

        stage('Run automated integration tests') {
            steps {
                sh "cd ../../../"
                sh "mvn clean test"
            }
        }

        stage('Stop and remove Docker instances') {
            steps {
                sh "docker stop $(docker ps -a -q)"
                sh "docker system prune --all"
                sh "yes"
            }
        }
    }
}
