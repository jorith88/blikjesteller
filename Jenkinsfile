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
        // Stop current docker container
        sh "docker stop blikjesteller || echo 'No blikjesteller container to stop'"

        // Remove the current container
        sh "docker rm blikjesteller || echo 'No blikjesteller container to delete'"

        // Create and start a new container with the latest version
        sh "docker run --name=blikjesteller -p 8181:80 -d --restart=always -v /etc/localtime:/etc/localtime -v /etc/timezone:/etc/timezone registry.jorith.nl/blikjesteller"
    }
}