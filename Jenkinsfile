node {
    checkout scm

    def app

    stage('Build frontend') {
        sh 'npm install'
        sh 'gulp'
    }

    stage('Build Docker image') {
        app = docker.build('registry.jorith.nl/blikjesteller')
    }

    stage ('Publish Docker image') {
        app.push('latest')
    }

    stage ('Deploy') {

    }
}