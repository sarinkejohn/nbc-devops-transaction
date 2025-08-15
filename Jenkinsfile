pipeline {
    agent {
        kubernetes {
            label 'maven-docker'
            yamlFile 'jenkins-podtemplate.yaml' 
        }
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub-sarinke')
        DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        IMAGE_TAG = "${env.GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts 'target/*.jar'
            }
        }
        stage('Docker Build & Push') {
            steps {
                sh "docker build -t ${DOCKER_REPO}:${IMAGE_TAG} ."
                sh "docker push ${DOCKER_REPO}:${IMAGE_TAG}"
                sh "docker tag ${DOCKER_REPO}:${IMAGE_TAG} ${DOCKER_REPO}:latest"
                sh "docker push ${DOCKER_REPO}:latest"
            }
        }
        stage('Deploy to Minikube') {
            steps {
                sh "kubectl apply -f k8s-deployment.yaml"
            }
        }
    }
}
