pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub-sarinke')
        DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'
        GIT_COMMIT_SHORT = "${env.GIT_COMMIT ?: sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
        IMAGE_TAG = "${GIT_COMMIT_SHORT}-${env.BUILD_NUMBER}"
        MAVEN_IMAGE = 'maven:3.9.9-eclipse-temurin-23'
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
                    image env.MAVEN_IMAGE
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    def imageName = "${env.DOCKER_REPO}:${env.IMAGE_TAG}"
                    
                    // Build using Docker Pipeline plugin
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-sarinke') {
                        def built = docker.build(imageName)
                        built.push()
                        built.push('latest')
                    }
                    
                    // Alternative direct docker commands (uncomment if preferred)
                    /*
                    sh """
                        docker build -t ${imageName} .
                        echo \$DOCKERHUB_CREDENTIALS_PSW | docker login -u \$DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker push ${imageName}
                        docker tag ${imageName} ${env.DOCKER_REPO}:latest
                        docker push ${env.DOCKER_REPO}:latest
                    """
                    */
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                sh "kubectl apply -f k8s-deployment.yaml"
            }
        }

        stage('Cleanup') {
            steps {
                sh """
                    docker rmi ${env.DOCKER_REPO}:${env.IMAGE_TAG} || true
                    docker rmi ${env.DOCKER_REPO}:latest || true
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "Successfully built and pushed: ${env.DOCKER_REPO}:${env.IMAGE_TAG}"
            slackSend(color: 'good', message: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
        }
        failure {
            echo "Pipeline failed"
            slackSend(color: 'danger', message: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
        }
    }
}