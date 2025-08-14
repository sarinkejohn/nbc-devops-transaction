pipeline {
    agent {
        kubernetes {
            label 'maven-kaniko-agent'
            defaultContainer 'maven'
            yamlFile 'pod-template.yaml'  // path to your pod template if stored in repo
        }
    }

    environment {
        DOCKER_REPO = 'sarinkejohn/nbc-devops-transaction'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        K8S_DEPLOYMENT = 'k8s-deployment.yaml'  // your deployment YAML
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                container('kaniko') {
                    sh """
                    /kaniko/executor \
                        --dockerfile=Dockerfile \
                        --context=dir://\$WORKSPACE \
                        --destination=${DOCKER_REPO}:${IMAGE_TAG} \
                        --destination=${DOCKER_REPO}:latest
                    """
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                container('maven') { // kubectl can run from any container with kubectl
                    sh 'kubectl apply -f ' + env.K8S_DEPLOYMENT
                }
            }
        }
    }

    post {
        success {
            echo "Build & deployment successful: ${DOCKER_REPO}:${IMAGE_TAG}"
        }
        failure {
            echo "Build or deploy failed"
        }
    }
}
