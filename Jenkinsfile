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
        // GitHub repository
        GITHUB_REPO = 'https://github.com/AkasbiYasser/jenkins_train.git' 
    }

    stages {    

        stage('Checkout from GitHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
                        sh 'git clone https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/AkasbiYasser/jenkins_train.git'
                    }
                }
            }
        }

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
