pipeline {
    agent {
        kubernetes {
            label 'maven-docker'
            yamlFile 'jenkins-podtemplate.yaml'
        }
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build with Maven') {
            container('maven') {
                sh 'mvn -B clean package -DskipTests'
            }
        }
        stage('Docker Build & Push') {
            container('maven-docker') {
                sh 'docker build -t sarinkejohn/sarinkejohnspringapp:latest .'
                sh 'docker push sarinkejohn/sarinkejohnspringapp:latest'
            }
        }
    }
}
