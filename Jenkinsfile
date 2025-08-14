pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = 'docker-hub-sarinke'                     
    DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'      
    // TAG will be: git short hash + build number
    GIT_COMMIT_SHORT = "${env.GIT_COMMIT ?: sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
    IMAGE_TAG = "${GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        script {
          // ensure GIT_COMMIT_SHORT is set when Jenkins injects GIT vars differently
          env.GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
          env.IMAGE_TAG = "${env.GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
        }
      }
    }

    stage('Build (Maven)') {
      steps {
        // run maven; remove -DskipTests if you want tests to run
        sh 'mvn -B clean package -DskipTests'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('Build Docker image') {
      steps {
        script {
          def imageName = "${env.DOCKER_REPO}:${env.IMAGE_TAG}"
          // Use Docker Pipeline plugin helper if available:
          docker.withRegistry('https://registry.hub.docker.com', env.DOCKERHUB_CREDENTIALS) {
            def built = docker.build(imageName)
            built.push()
            // also push latest tag (optional)
            docker.tag(imageName, "${env.DOCKER_REPO}:latest")
            docker.image("${env.DOCKER_REPO}:latest").push()
          }
        }
      }
    }

    stage('Cleanup local images') {
      steps {
        sh "docker rmi ${env.DOCKER_REPO}:${env.IMAGE_TAG} || true"
        sh "docker rmi ${env.DOCKER_REPO}:latest || true"
      }
    }
  }

  post {
    success {
      echo "Image pushed: ${env.DOCKER_REPO}:${env.IMAGE_TAG}"
    }
    failure {
      echo "Build or push failed"
    }
  }
}
