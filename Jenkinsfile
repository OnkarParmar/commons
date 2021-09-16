pipeline {
    agent any

    environment {
        REPO = "333490196116.dkr.ecr.ap-south-1.amazonaws.com"
        PROJECT = "teamteach-commons"
        USER = "ec2-user"
    }
    
    stages{
        stage('Build') {
            steps {
                sh 'echo $GIT_BRANCH'
                sh "ls -l"
            }
        }
        stage('Package') {
            steps {
                sh 'mvn install:install-file -Dfile=target/teamteach-commons-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
