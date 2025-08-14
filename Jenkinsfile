pipeline {
    agent {
        kubernetes {
            label 'maven-kaniko-agent'
            defaultContainer 'jnlp'
            yamlFile 'pod-template.yaml' // reference your pod template file
        }
    }

    environment {
        IMAGE_NAME = "sarinkejohn/nbc-devops-transaction"
        IMAGE_TAG  = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                container('maven') {
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                container('kaniko') {
                    sh """
                    /kaniko/executor \
                        --context=/workspace \
                        --dockerfile=/workspace/Dockerfile \
                        --destination=${IMAGE_NAME}:${IMAGE_TAG} \
                        --cache=true
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully."
        }
        failure {
            echo "Pipeline failed."
        }
    }
}
