pipeline {
    agent any
    tools { maven 'maven-3' }

    stages {
        stage('clean and download code') {
            steps {
                cleanWs()
                sh 'rm -rf backend/.scannerwork'
                sh 'rm -rf backend/target'
                checkout scm
            }
        }
        
        stage('compile and analize') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('sonar-server') {
                        sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=fast-tasker -Dsonar.ws.timeout=300'
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
