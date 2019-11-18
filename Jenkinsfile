pipeline {
    agent any
    tools {
        maven 'mvn'
        jdk 'java11'
        git 'Default'
    }
    stages {
        stage('Maven build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build Image') {
            steps {
                sh 'docker build -t leeflangjs/test-with-pipeline .'
            }
        }
        stage('Push') {
            environment {
                DOCKER_CREDS = credentials('test-docker-secret')
            }
            steps {
                sh 'docker login -u $DOCKER_CREDS_USR -p $DOCKER_CREDS_PSW && docker push leeflangjs/test-with-pipeline'
            }
        }
    }
}