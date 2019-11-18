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
            sh 'docker build -t leeflangjs/test-with-pipeline'
            }
        }
//         stage('Push') {
//             steps {
//             //
//             }
//         }
//         stage('Deploy') {
//             steps {
//             //
//             }
//         }
    }
}