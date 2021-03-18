pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                echo 'build'
            //sh './gradlew build -x test'
            }
        }
        stage('test') {

            //sh './gradlew check'
            
            steps {
                    sh 'docker build -t harrytaorui/basic-app:1.0.0 .'
                    sh 'docker images'
                }
        }
        stage('staging') {
            steps {
                echo 'stage staging'
            }
        }
    }
}
