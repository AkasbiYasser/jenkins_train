def call(String dockerRegistry, String dockerImageName, String dockerTag, String workspace) {
    echo "Building Docker image..."
    dir("${workspace}/jenkins_train") {
        sh "docker build -t ${dockerRegistry}/${dockerImageName}:${dockerTag} ."
    }
}
