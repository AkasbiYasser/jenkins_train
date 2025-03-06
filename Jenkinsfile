@Library('my-shared-library') _


pipeline {
    agent any

    parameters {
        string(name: 'DOCKER_REGISTRY', defaultValue: 'akasbiyasser', description: 'Docker Registry username')  
        string(name: 'DOCKER_TAG', defaultValue: 'v1.0.0', description: 'Tag for the Docker image')  
        string(name: 'DOCKER_IMAGE_NAME', defaultValue: 'java-mvn-app', description: 'Name for the Docker image')  
    }

    stages {    

        stage('Checkout from GitHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
                        sh "git clone https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/AkasbiYasser/jenkins_train.git ${WORKSPACE}/jenkins_train"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    buildProject("${WORKSPACE}")
                }
            }
        }

        stage('Generate Artifact') {
            steps {
                script {
                    generateArtifact("${WORKSPACE}")
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    buildDockerImage("${DOCKER_REGISTRY}", "${DOCKER_IMAGE_NAME}", "${DOCKER_TAG}", "${WORKSPACE}")
                }
            }
        }

        stage('Confirm Push Docker Image') {
            steps {
                input message: 'Do you want to push the Docker image to the registry?', ok: 'Yes'
            }
        }

        stage('Login to Docker Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'akasbi-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh '''
                            echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                        '''
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh "docker push ${params.DOCKER_REGISTRY}/${params.DOCKER_IMAGE_NAME}:${params.DOCKER_TAG}"
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh "docker rmi ${params.DOCKER_REGISTRY}/${params.DOCKER_IMAGE_NAME}:${params.DOCKER_TAG}"
            }
        }
    }
}
