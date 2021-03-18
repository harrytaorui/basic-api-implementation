pipeline {
    agent {
        docker {image 'mysql:latest'}
    }

    stages {
        stage('build') {
            steps {
                echo 'build'
            //sh './gradlew build -x test'
            }
        }
        stage('test') {
            agent {
                docker {
                    image 'mysql:latest'
                    args '-e "MYSQL_ROOT_PASSWORD=root" -p 3306:3306'
                }

            //sh './gradlew check'
            }
            steps {
                    sh 'mysql -v'
                }
        }
        stage('staging') {
            steps {
                echo 'stage staging'
            }
        }
    }
}
