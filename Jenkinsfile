pipeline {
    agent any

    tools {
        // Estos nombres deben coincidir con lo que configures en 
        // "Manage Jenkins" -> "Global Tool Configuration"
        jdk 'Java21'
        maven 'Maven3'
    }

    environment {
        // Definimos las variables que usa el comando de Maven
        SONAR_PROJECT_KEY = 'kimyork32_fast-tasker_ca1abb76-2c67-4324-9a8e-b18d4f2cccf8'
        // Es mejor guardar el token en Jenkins Credentials y llamarlo aquí
        SONAR_TOKEN = credentials('sonar-token-id') 
        SONAR_HOST_URL  = 'https://alexandria-subobscure-luella.ngrok-free.dev'
    }

    stages {
        stage('Checkout') {
            steps {
                // Descarga el código de GitHub
                checkout scm
            }
        }

        stage('Build & Sonar Analysis') {
            steps {
                // Entramos a la carpeta backend tal como hacías en GitHub Actions
                dir('backend') {
                    script {
                        // Ejecutamos el comando de Maven con los mismos parámetros
                        sh """
                            mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.token=${SONAR_TOKEN} \
                            -Dsonar.host.url=${SONAR_HOST_URL}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Limpia el espacio de trabajo al finalizar
            cleanWs()
        }
        success {
            echo '¡Análisis completado con éxito!'
        }
        failure {
            echo 'El pipeline falló. Revisa los logs de Maven.'
        }
    }
}
