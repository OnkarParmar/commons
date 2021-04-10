#!groovy

node {

    try{

        bitbucketStatusNotify(buildState: 'INPROGRESS')
       // fetching git repo
       stage('Preparation') {
         git url: 'https://sanky-cse@bitbucket.org/xlrt_production/common_services.git',
            credentialsId: 'bitbucket-credential', branch: 'development'

       }
       // running test cases
       stage('test') {
         echo "test started============================================"
         def myTestContainer = docker.image('maven:3.6.2-jdk-11-slim')
            myTestContainer.pull()
            myTestContainer.inside {
            sh 'mvn test'

            }
          def content = "Test cases done"
          echo "test finished  ========================================="
       }

       stage('Sonarqube') {
               def scannerHome = tool 'SonarQubeScanner'
               withSonarQubeEnv('sonarqube') {
                   sh "${scannerHome}/bin/sonar-scanner"
               }
               timeout(time: 10, unit: 'MINUTES') {
                     def qg = waitForQualityGate()
                     if (qg.status != 'OK') {
                         bitbucketStatusNotify(buildState: 'FAILED')
                         error "Pipeline aborted due to quality gate failure: ${qg.status}"
                     }

               }

       }
       stage('docker build/push') {

             docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
               def app = docker.build("prmxlrt/prm-xlrt-common-service:production", "-f Dockerfile_CI").push()
             }

       }
       bitbucketStatusNotify(buildState: 'SUCCESSFUL')
        def content = "build successful"
        emailext(body: content, mimeType: 'text/html',
                replyTo: '$DEFAULT_REPLYTO', subject: 'Test Report Common service',
                to: '$DEFAULT_RECIPIENTS', attachLog: true )
   }
   catch(e){
       currentBuild.result = "FAILURE";
       // set variables
       def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} ${currentBuild.result}"
       def content = '${JELLY_SCRIPT,template="html"}'
       emailext(body: content, mimeType: 'text/html',
               replyTo: '$DEFAULT_REPLYTO', subject: subject,
               to: '$DEFAULT_RECIPIENTS', attachLog: true )
        // mark current build as a failure and throw the error
        throw e
        bitbucketStatusNotify(buildState: 'FAILED')

   }
}