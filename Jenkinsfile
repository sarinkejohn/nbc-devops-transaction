pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-23'
            args '-v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub-sarinke')
        DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'
        IMAGE_TAG = "${env.GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                }
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
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-sarinke') {
                        def image = docker.build("${env.DOCKER_REPO}:${env.IMAGE_TAG}")
                        image.push()
                        image.push('latest')
                    }
                }
            }
        }
    }
}