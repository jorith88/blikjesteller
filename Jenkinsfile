node {
    checkout scm

    stage('Build frontend') {
        dir('web') {
            sh 'npm install'
            sh 'gulp'
            sh 'gulp cacheBuster'
        }
    }

    stage('Build API') {
        dir('api') {
            sh 'mvn clean package'
        }
    }

    stage ('Deploy') {
        sh "docker-compose up -d"
        emailext body: 'Deployed new version to production.', subject: 'Blikjesteller', to: 'jorith@gmail.com'
    }
}