pipeline {
    agent {
      label "master"
    }

    options {
      timeout(time: 10, unit: 'MINUTES')
    }

    triggers {
      cron ('H 0 * * *')
    }

    stages {
        stage('BUILD') {
            steps {
                sh '''
                mvn clean verify
                '''
            }
        }
    }
}
