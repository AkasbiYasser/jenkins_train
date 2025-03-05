pipeline {
    agent any
    
    tools {
        maven 'mvn-3.9.6'
    }

    parameters {
        string(name: 'DOCKER_TAG', defaultValue: 'v1.0.0', description: 'Tag for the Docker image')
        string(name: 'DOCKER_IMAGE_NAME', defaultValue: 'java-mvn-app', description: 'Name for the Docker image')
        string(name: 'DOCKER_REGISTRY', defaultValue: 'akasbiyasser', description: 'Docker Registry username')
        string(name: 'GITHUB_REPO', defaultValue: 'https://github.com/AkasbiYasser/jenkins_train.git', description: 'GitHub repository URL')
    }

    stages {    

        stage('Checkout from GitHub') {
            steps {
                script {
                    // Demander à l'utilisateur de confirmer ou changer l'URL du dépôt GitHub
                    GITHUB_REPO = input message: 'Please confirm the GitHub repository URL or change it:', 
                                         parameters: [string(defaultValue: 'https://github.com/AkasbiYasser/jenkins_train.git', description: 'GitHub Repository URL', name: 'GITHUB_REPO')]

                    // Vérifier si l'URL contient déjà "https://", sinon, l'ajouter
                    if (!GITHUB_REPO.startsWith('https://')) {
                        GITHUB_REPO = "https://${GITHUB_REPO}"
                    }

                    // Cloner le dépôt GitHub en utilisant les identifiants et l'URL corrigée
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
                        sh "git clone https://${GITHUB_USER}:${GITHUB_TOKEN}@${GITHUB_REPO}"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh '''
                   cd $WORKSPACE/jenkins_train
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
                script {
                    sh """
                       cd $WORKSPACE/jenkins_train
                       docker build -t ${params.DOCKER_REGISTRY}/${params.DOCKER_IMAGE_NAME}:${params.DOCKER_TAG} . 
                    """
                }
            }
        }

        stage('Login to Docker Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'akasbi-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                        """
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh """
                        docker push ${params.DOCKER_REGISTRY}/${params.DOCKER_IMAGE_NAME}:${params.DOCKER_TAG}
                    """
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh """
                    docker rmi ${params.DOCKER_REGISTRY}/${params.DOCKER_IMAGE_NAME}:${params.DOCKER_TAG}
                """
            }
        }
    }
}
