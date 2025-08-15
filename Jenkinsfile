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
        tage('Docker Build & Push') {
        steps {
            container('docker') {
            sh """
                docker build -t ${DOCKER_REPO}:${IMAGE_TAG} .
                echo "${DOCKERHUB_CREDENTIALS_PSW}" | docker login -u "${DOCKERHUB_CREDENTIALS_USR}" --password-stdin
                docker push ${DOCKER_REPO}:${IMAGE_TAG}
                docker tag ${DOCKER_REPO}:${IMAGE_TAG} ${DOCKER_REPO}:latest
                docker push ${DOCKER_REPO}:latest
            """
            }
        }
        }
        stage('Deploy to Minikube') {
            steps {
                sh "kubectl apply -f k8s-deployment.yaml"
            }
        }
    }
}
