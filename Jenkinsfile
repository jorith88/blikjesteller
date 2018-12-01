node {

    checkout scm

    stage('Build') {
        sh 'npm install'
        sh 'gulp'
        sh 'gulp cacheBuster'
    }

    stage ('Deploy') {
        sh "docker-compose up -d"
        emailext body: 'Deployed new version to production.', subject: 'Blikjesteller', to: 'jorith@gmail.com'
    }
}