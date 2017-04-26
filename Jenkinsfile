node {

    checkout scm
    def app

    stage('Build frontend') {
        sh 'npm install'
        sh 'gulp'
    }

    stage('Build Docker image') {
        app = docker.build('registry.jorith.nl/blikjesteller', '--pull --no-cache .')
    }

    stage ('Publish Docker image') {
        app.push('latest')
    }

    stage ('Deploy') {

        try {
            sh "docker stop blikjesteller"
            sh "docker rm blikjesteller"
        } catch (Exception _) {
            echo "no container to stop"
        }

        sh "docker run --name=blikjesteller -p 8181:80 -d --restart=always -v /etc/localtime:/etc/localtime -v /etc/timezone:/etc/timezone registry.jorith.nl/blikjesteller"
    }
}