def AWS_KEY = "work-laptop.pem"
def ECR_PASSWORD = "eyJwYXlsb2FkIjoiOEl2bUNuamVSbm15eld0RlVvM09XWUMyWkRreFNZalU5eHJHL1VGMGRkZnVtdThkWGVCZnJtOVRIb2lVTDdkdjM1VkhvNE5VU0lyMlltOWVONWhoeFFrWmpOVWFUN1dtUGthai9pWjdHNXRXRDJEQi9UQ3BRbElVWFlsM2lMMjBZc3BQNTBtOVY3ZGptZjVObXVzTllCOS8xemE1NTMyYytOTVV4RGlwbXZ3TEVjYnp6ZTBiZDNuL3ptMHlQa0xNaDBiSisvUi9FeURwZTc0NzNLOVhMc3VadGt4Z01KK0hrSENmOFZTcnQ1SGRNYkNLU0hBUVJBKzlscWxtWWdtNmU4Y0pwV2pQWk5rbEZoNzZjSDBUOW93dUYrUUNDTzFGc3IzMkhVMU02ZEJTWXZIN21HSEFCZlRxeXl3cFB2bDZ4a3E0cTVtL0U3UmEyT3I4RUlyMjdrcW1ENUJXdy9ib3FSczlRSjRnRytZTGtuVTg2eHkxaWtkRGJ3eGYxNGJ3MzJXdGFjN1VzWmJSQTlmdFZ1N3k4ZmFESUYza0xYbUhjQmt4UDNZcE1GZFhua0ZtU0l4K0dFbFkwcUxGT0RuVjZOSHpabGs4dWRTQUhxYnBvS2hVaXkrOS9HZEZ3SXhvVU0zdGpnbGRxeGFRVWVFb0crcWY0Lys5cXUxZlZuTXZrZ1Bqak45ZGtqS00raThHaGZwOHRlTS9WVXBiSmVNQk5yeU1Ba2JEeUt2SENPb0FsZDRoVFRTZ1pacE1WWEJnTFFLYWw0R1NpNFkrcUZ0K1lhRHFSVk04SVBrelJIUnZyRFhZbTBsZ2lyaWlSM0wxa0lidXRCRzBGNnZhVXlsQzVTU0x0TndxbE1sbkY1dDVNSlBnWmxnUTJMQTZycFlybVd0VEN2eWJYaDFSY0VidTlrSE9kSmdxU2tJL2svQUd4SFd4Q3M3aFJNUmI2cjgwV0FuT1NxVUhhMFJpWGNRMHlJZktkK0JXbHptMjVmQXVBdEIrYjFPU1RoTGRFM3VpN0pXbWZLUCtWaVo4T1k0cGU5MUU0ck9xOVdnVGkyZXRibUV6VnZ1Q25oQ09sWWJGRUVsUGdwMVNDQ3FYc1RteDBYVlBnS0k1aU1EY0dlUVNlSHFpN002MWkrVlJuaXY4REY3V3hQT3hVVGpyK09rejBNcHIrN3EreXE5aFFzamhJek5EeVFsR3VWWTUxOXllcTNISkhUdGw5ZHBtcmozNUhRRU9PdEYxNXM1WWpzT05ScUZTQStLcW4vaSthbkVIRFZjQjh6ZlFMS2F5RFJ2QmpER0g3YmlaeDB1VGlxdWlKNmZHRlZXSDhIelhwM1NNSE1PbHVvVi9JcGV6aEgyUWhVNS9kb0Z2Z1JiQmtuQkxINVBvK0dCSWFxcHVSMWNhM2FRN0JNUldJNFJRWlRUQkNTd1FVbkxGeTdaYWFyaDJRZnlhWTkrNmVUZTkzOERva05Jb0cvaUVTREdBSnR2VFRraHJ5SHpLNzZzRWRiZXhUdnhhaWdwTFZCc29TZXB1RmJaVEVEeXdlL01mS0M0S2VhcjN5QkF6IiwiZGF0YWtleSI6IkFRRUJBSGh3bTBZYUlTSmVSdEptNW4xRzZ1cWVla1h1b1hYUGU1VUZjZTlScTgvMTR3QUFBSDR3ZkFZSktvWklodmNOQVFjR29HOHdiUUlCQURCb0Jna3Foa2lHOXcwQkJ3RXdIZ1lKWUlaSUFXVURCQUV1TUJFRURMTC9oRXlLanN5dmxUOHpUZ0lCRUlBN3lZa2ZTTEJETWJnam5YU2IxaUN2YXNmRzljODkvZmk5RDAycFdsODJnOTIxV1I3SVBWSGx1b0ZQQW9Ua005UGtwVExjcVJ6THRRNHpzekk9IiwidmVyc2lvbiI6IjIiLCJ0eXBlIjoiREFUQV9LRVkiLCJleHBpcmF0aW9uIjoxNjAzMjM2MDIyfQ=="
def EC2_IP = "54.166.212.119"

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
                sh "sudo docker login -u AWS -p ${ECR_PASSWORD} https://571845120151.dkr.ecr.us-east-1.amazonaws.com"
                sh '''sudo docker tag resources_auth 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_auth
                sudo docker push 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_auth'''
                sh '''sudo docker tag resources_db 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_db
                sudo docker push 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_db'''
                sh '''sudo docker tag redis 571845120151.dkr.ecr.us-east-1.amazonaws.com/redis
                sudo docker push 571845120151.dkr.ecr.us-east-1.amazonaws.com/redis'''
            }
        }

        stage('Deploy to EC2') {
            steps {
                sh "ssh -o StrictHostKeyChecking=no -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_auth"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_db"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker pull 571845120151.dkr.ecr.us-east-1.amazonaws.com/redis"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker network create --subnet=172.0.0.0/16 grid"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 8081:8081 --net grid --name auth_ms 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_auth"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 8082:8082 --net grid --ip 172.0.0.3 --name db_ms 571845120151.dkr.ecr.us-east-1.amazonaws.com/resources_db"
                sh "ssh -i ${AWS_KEY} ec2-user@${EC2_IP} docker run -d -p 6379:6379 --net grid --ip 172.0.0.4 --hostname 172.0.0.4 --name redis_db 571845120151.dkr.ecr.us-east-1.amazonaws.com/redis"
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
