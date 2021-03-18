pipeline {
  agent any
  stages {
    
    stage("build") {
      steps {
        sh './gradlew build -x test'
      }
      
    }
    stage("test") {
      steps {
        docker.image('mysql:5').withRun('-e "MYSQL_ROOT_PASSWORD=root" -p 3306:3306') { c ->
          sh 'while ! mysqladmin ping -h0.0.0.0 --silent; do sleep 1; done'
          sh 'make check'
        }
        sh './gradlew check'
      } 
    }
    stage("staging") {
      steps {
        echo 'stage staging'
      } 
    }
  
  }
  post {
  
    always {
      //junit 'build/reports/**/*.xml'
    }
  }

}
