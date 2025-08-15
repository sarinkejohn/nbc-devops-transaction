pipeline {
    agent any

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
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-23'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('Docker Build & Publish') {
            steps {
                dockerBuildAndPublish(
                    buildContext: '.',
                    dockerRegistryCredentials: env.DOCKERHUB_CREDENTIALS,
                    repoName: env.DOCKER_REPO,
                    tag: env.IMAGE_TAG,
                    pushLatest: true,
                    forceTag: true,
                    dockerfilePath: 'Dockerfile'
                )
            }
        }

        stage('Deploy to Minikube') {
            steps {
                sh "kubectl apply -f k8s-deployment.yaml"
            }
        }
    }

    post {
        cleanup {
            sh "docker rmi ${env.DOCKER_REPO}:${env.IMAGE_TAG} || true"
            cleanWs()
        }
    }
}