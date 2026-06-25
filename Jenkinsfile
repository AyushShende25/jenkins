pipeline {
  agent { label 'docker-maven-trivy'}
  tools {
    maven 'maven3'
  }
  environment {
    SONAR_IP = '172.31.11.106'
    ECR_REGISTRY = '651447471372.dkr.ecr.ap-south-1.amazonaws.com'
    IMAGE_REPO = "$ECR_REGISTRY/devsecops-demo"
    TRIVY_CACHE_DIR = '/var/lib/trivy'
    TMPDIR = '/var/tmp'
  }
  stages {
  stage('Trivy FS scan') {
    steps {
        sh '''
            trivy fs \
              --cache-dir $TRIVY_CACHE_DIR \
              --exit-code 1 \
              --severity HIGH,CRITICAL \
              .
        '''
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
    stage('Build Image') {
        steps {
          sh 'export DOCKER_BUILDKIT=0 && docker build --platform linux/amd64 -t "$IMAGE_REPO:$BUILD_NUMBER" -t "$IMAGE_REPO:latest" .'
      }
    }
  stage('Trivy Image Scan') {
    steps {
        sh '''
            trivy image \
              --cache-dir $TRIVY_CACHE_DIR \
              --exit-code 0 \
              --severity HIGH,CRITICAL \
              "$IMAGE_REPO:$BUILD_NUMBER"
        '''
    }
}
stage('Push to ECR') {
  steps {
    sh 'docker push "$IMAGE_REPO:$BUILD_NUMBER"'
    sh 'docker push "$IMAGE_REPO:latest"'
  }
}
  }
}