pipeline {
    agent any

    environment {
        REPO = "333490196116.dkr.ecr.ap-south-1.amazonaws.com"
        PROJECT = "teamteach-commons"
        USER = "ec2-user"
        DOMAIN = "myfamilycoach.ml"
    }
    
    stages{
        stage('Build') {
            steps {
                sh 'echo $GIT_BRANCH'
                sh "mvn install -Ddocker -Dbranch=${GIT_BRANCH}"
            }
        }
        stage('Package') {
            steps {
                sh 'mvn install:install-file -Dfile=target/teamteach-commons-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
