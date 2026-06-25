pipeline {
  agent { label 'docker-maven-trivy'}
  tools {
    maven 'maven3'
  }
  environment {
    SONAR_IP = '172.31.11.106'
    ECR_REGISTRY = '651447471372.dkr.ecr.ap-south-1.amazonaws.com'
  }
  stages {
    stage('Trivy FS scan') {
      steps {
        sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL .'
      }
    }
    stage('Build & Sonar') {
      steps {
        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                   sh 'mvn clean verify sonar:sonar \
  -Dsonar.projectKey=cwvj-devsecops-demo \
  -Dsonar.host.url="http://${SONAR_IP}:9000" \
  -Dsonar.token="${SONAR_TOKEN}" \
  -Dsonar.qualitygate.wait=true'
        }
      }
    }
    stage('ECR login') {
      steps {
        sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin $ECR_REGISTRY'
      }
    }
  }
}