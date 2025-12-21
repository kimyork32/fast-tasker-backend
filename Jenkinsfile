pipeline {
    agent any
    tools { maven 'maven-3' }

    stages {
        stage('Descargar Código') {
            steps {
                checkout scm
            }
        }
        
        stage('Compilar y Analizar') {
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
