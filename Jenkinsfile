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
//         stage('Build Image') {
//             steps {
//             //
//             }
//         }
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