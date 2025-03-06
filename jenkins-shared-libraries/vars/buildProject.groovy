def call(String workspace) {
    echo "Building the project with Maven..."
    dir("${workspace}/jenkins_train") {
        sh "mvn clean install"
    }
}
