pipeline {
    agent none 

    stages {
        // -----------------------------------------------------------
        // PASO 0: COMÚN (Se ejecuta siempre, para todos)
        // -----------------------------------------------------------
        stage('Setup Code') {
            agent any 
            steps {
                cleanWs()
                checkout scm
                stash name: 'source-code', includes: '**' 
            }
        }
        
        // -----------------------------------------------------------
        // CAMINO A: Flujo para DEVELOP (Tu lógica original)
        // -----------------------------------------------------------
        stage('CI Flow (Develop)') {
            // Esta es la magia: Solo entra aquí si es rama develop o PR hacia develop
            when {
                anyOf {
                    branch 'develop'
                    changeRequest target: 'develop'
                }
            }
            stages {
                stage('Parallel Analysis') {
                    parallel {
                        stage('Monolith Build') {
                            agent any 
                            tools { maven 'maven-3' } 
                            steps {
                                cleanWs()
                                unstash 'source-code' 
                                dir('monolith-app') {
                                    sh 'rm -rf .scannerwork target'
                                    withSonarQubeEnv('sonar-server') {
                                        sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=fast-tasker-monolith -Dsonar.ws.timeout=300'
                                    }
                                }
                            }
                        }

                        stage('Notification Build') {
                            agent any 
                            tools { maven 'maven-3' }
                            steps {
                                cleanWs()
                                unstash 'source-code'
                                dir('fast-tasker-notification') {
                                    sh 'rm -rf .scannerwork target'
                                    withSonarQubeEnv('sonar-server') {
                                        sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=fast-tasker-notification -Dsonar.ws.timeout=300'
                                    }
                                }
                            }
                        }
                    }
                }
                
                stage('Quality Gate') {
                    agent any 
                    steps {
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }
            }
        }

        // -----------------------------------------------------------
        // CAMINO B: Flujo para STAGING (Seguridad y Performance)
        // -----------------------------------------------------------
        stage('Staging Checks') {
            // Solo entra aquí si el PR apunta a la rama 'staging'
            when {
                changeRequest target: 'staging'
            }
            // También lo hacemos en paralelo para ser eficientes
            parallel {
                stage('Security Scan (SAST/DAST)') {
                    agent any
                    steps {
                        cleanWs()
                        unstash 'source-code'
                        echo "--- EJECUTANDO TEST DE SEGURIDAD ---"
                        // Ejemplo: OWASP Dependency Check o Trivy
                        sh 'echo "Running Trivy or OWASP..."' 
                    }
                }

                stage('Performance Tests') {
                    agent any
                    steps {
                        cleanWs()
                        unstash 'source-code'
                        echo "--- EJECUTANDO TEST DE PERFORMANCE ---"
                        // Ejemplo: JMeter o k6
                        sh 'echo "Running k6 / JMeter tests..."'
                    }
                }
            }
        }
    }
}
