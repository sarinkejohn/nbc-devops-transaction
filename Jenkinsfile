pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: maven-kaniko-agent
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    args: ['\$(JENKINS_SECRET)', '\$(JENKINS_NAME)']
    resources:
      requests:
        cpu: "250m"
        memory: "512Mi"
      limits:
        cpu: "500m"
        memory: "1Gi"
  - name: maven
    image: maven:3.9.9-eclipse-temurin-23
    command:
    - cat
    tty: true
    volumeMounts:
      - name: workspace-volume
        mountPath: /workspace
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    command:
    - cat
    tty: true
    volumeMounts:
      - name: workspace-volume
        mountPath: /workspace
      - name: kaniko-secret
        mountPath: /kaniko/.docker
  volumes:
    - name: workspace-volume
      emptyDir: {}
    - name: kaniko-secret
      secret:
        secretName: dockerhub-secret
"""
        }
    }

    stages {
        stage('Checkout') {
            steps {
                container('maven') {
                    git url: 'https://github.com/sarinkejohn/nbc-devops-transaction.git', branch: 'main'
                }
            }
        }

        stage('Build (Maven)') {
            steps {
                container('maven') {
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                container('kaniko') {
                    sh '''
                    /kaniko/executor \
                      --context=/workspace \
                      --dockerfile=/workspace/Dockerfile \
                      --destination=sarinkejohn/nbc-devops-transaction:latest \
                      --verbosity=info
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished'
        }
    }
}
