pipeline {
    agent any
    stages {
        stage('build') {
            agent {
                docker {
                    image 'openjdk:11'
                    args '-v"$PWD":/app'
                    reuseNode true
                }
            }
            steps {
                sh './gradlew clean build'
            }
        }
    }
}
