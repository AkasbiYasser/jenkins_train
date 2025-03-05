pipeline {
    agent any
    
    tools {
        maven 'mvn-3.9.6'
    }

    environment {
        // Docker Hub username :
        DOCKER_REGISTRY = 'akasbiyasser'  
        // Docker image tag :
        DOCKER_TAG = 'v1.0.0'  
        // Docker image name :
        DOCKER_IMAGE_NAME = 'java-mvn-app'  
    }

    stages {    

        stage('Build') {
            steps {
                sh '''
                   cd $WORKSPACE/jenkins_train  
                   pwd
                   ls 
                   mvn clean install
                '''
            }
        }

        stage('Generate Artifact') {
            steps {
                sh '''
                   cd $WORKSPACE/jenkins_train 
                   mvn package
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                   cd $WORKSPACE/jenkins_train  
                   docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                '''
            }
        }

        stage('Login to Docker Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'akasbi-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh "docker rmi ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
            }
        }
    }
}
