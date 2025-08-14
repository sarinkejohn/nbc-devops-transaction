pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'docker-hub-sarinke'
        DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'
        GIT_COMMIT_SHORT = "${env.GIT_COMMIT ?: sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
        IMAGE_TAG = "${GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    env.IMAGE_TAG = "${env.GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Build (Maven)') {
            agent {
                docker {
                    image 'maven:3.8.8-openjdk-23'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker image') {
            steps {
                script {
                    def imageName = "${env.DOCKER_REPO}:${env.IMAGE_TAG}"
                    docker.withRegistry('https://registry.hub.docker.com', env.DOCKERHUB_CREDENTIALS) {
                        def built = docker.build(imageName)
                        built.push()
                        docker.image(imageName).push("latest")
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

        stage('Deploy to Minikube') {
            steps {
                sh "kubectl apply -f k8s-deployment.yaml"
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
