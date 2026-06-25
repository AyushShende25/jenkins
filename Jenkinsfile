pipeline {
  agent { label 'docker-maven-trivy'}
  stages {
    stage('Trivy FS scan') {
      steps {
        sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL .'
      }
    }
  }
}