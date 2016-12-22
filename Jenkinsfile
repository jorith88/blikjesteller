node {
    checkout scm

    stage('Build frontend') {
        sh 'npm install'
        sh 'gulp'
    }

    stage('Build Docker image') {
        def app = docker.build('blikjesteller')
    }

    stage ('Publish Docker image') {
        app.push('latest')
    }

    stage ('Deploy') {

    }
}