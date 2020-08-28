node {
    checkout scm

    stage('Build frontend') {
        dir('web') {
           def web = docker.build("registry.jorith.nl/blikjesteller/web")
           web.push()
        }
    }

    stage('Build API') {
        dir('api') {
            def api = docker.build("registry.jorith.nl/blikjesteller/api")
            api.push()
        }
    }
}