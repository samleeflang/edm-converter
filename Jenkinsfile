properties([pipelineTriggers([githubPush()])])
podTemplate(label: 'jenkins-slave', containers: [
    containerTemplate(name: 'git', image: 'alpine/git', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.6-jdk-11-slim', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', command: 'cat', ttyEnabled: true)
  ],
  volumes: [
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
  ]
  ) {
    node('jenkins-slave') {
        git url: 'https://github.com/samleeflang/edm-converter.git', branch: 'master'

        stage('Automatical deploy to kubernetes') {
            container('kubectl') {
                dir('edm-converter/') {
                    sh 'kubectl version'
                }
            }
        }

        stage('Clone repository') {
            container('git') {
                sh 'git clone -b master https://github.com/samleeflang/edm-converter.git'
            }
        }

        stage('Maven Build') {
            container('maven') {
                dir('edm-converter/') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Docker Build & Push') {
            container('docker') {
                dir('edm-converter/') {
                    sh 'docker login clariahacr.azurecr.io -u clariahacr -p tzoR8bw5CX39qntCJ+4DtUiHwkgUDgCy'
                    sh 'docker build . -t clariahacr.azurecr.io/leeflangs-test'
                    sh 'docker push clariahacr.azurecr.io/leeflangs-test'
                }
            }
        }

    }
}
