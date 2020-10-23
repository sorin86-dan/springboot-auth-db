def AWS_KEY = "<aws .pem key>"
def ECR_BASE = "<ECR BASE URL>"
def ECR_PASSWORD = "<Docker password>"
def EC2_IP = "<EC2 IP>"

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

        stage('Build and run Docker instances') {
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

        stage('Push containers to ECR') {
            steps {
                sh "sudo docker login -u AWS -p ${ECR_PASSWORD} https://${ECR_BASE}"
                sh "sudo docker tag resources_auth ${ECR_BASE}/resources_auth"
                sh "sudo docker push ${ECR_BASE}/resources_auth"
                sh "sudo docker tag resources_db ${ECR_BASE}/resources_db"
                sh "sudo docker push ${ECR_BASE}/resources_db"
                sh "sudo docker tag redis ${ECR_BASE}/redis"
                sh "sudo docker push ${ECR_BASE}/redis"
            }
        }

        stage('Deploy to EC2') {
            steps {
                sh "ssh -o StrictHostKeyChecking=no -i ${AWS_KEY} ec2-user@${EC2_IP} docker login -u AWS -p ${ECR_PASSWORD} https://${ECR_BASE}"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull ${ECR_BASE}/resources_auth"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull ${ECR_BASE}/resources_db"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull ${ECR_BASE}/redis"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker network create --subnet=172.0.0.0/16 grid"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 8081:8081 --net grid --name auth_ms ${ECR_BASE}/resources_auth"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 8082:8082 --net grid --ip 172.0.0.3 --name db_ms ${ECR_BASE}/resources_db"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 6379:6379 --net grid --ip 172.0.0.4 --hostname 172.0.0.4 --name redis_db ${ECR_BASE}/redis"
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
