pipeline {
    agent any
    stages {
        stage('Build The Image') {
            steps {
                sh 'mvn clean package -DskipTests'
                sh 'mvn install:install-file -Dfile=target/teamteach-commons-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
