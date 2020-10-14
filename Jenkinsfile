pipeline {

    agent any

    stages {
        stage('Retrieve code') {
            steps {
                git 'https://github.com/sorin86-dan/springboot-auth-db'
            }
        }

        stage('Build code') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build and run apps') {
            steps {
                dir ('src/test/resources') {
                  sh 'sudo docker-compose up -d'
                }
            }
        }

        stage('Run automated tests') {
            steps {
                sh 'if ! [ "$(sudo docker network inspect resources_grid | grep jenkins-container)" ]; then sudo docker network connect resources_grid jenkins-container; fi'
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            sh 'sudo docker stop redis-db db-ms auth-ms'
            sh 'sudo docker system prune -a -f'
            step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
        }
    }
}
