def call(String workspace) {
    echo "Generating the artifact..."
    dir("${workspace}/jenkins_train") {
        sh "mvn package"
    }
}
