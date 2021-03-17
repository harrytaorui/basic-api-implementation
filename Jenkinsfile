pipeline {
  agent any
  stages {
    
    stage("build") {
      steps {
        sh './gradlew build'
      }
      
    }
    stage("test") {
      steps {
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
      junit 'build/reports/**/*.xml'
    }
  }

}
