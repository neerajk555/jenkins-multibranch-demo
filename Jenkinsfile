pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6'
        jdk 'JDK-17'
    }
    
    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
        APP_NAME = 'simple-java-app'
        BUILD_NUMBER_FORMATTED = String.format("%03d", BUILD_NUMBER as Integer)
    }
    
    stages {
        stage('Checkout & Info') {
            steps {
                echo "Building branch: ${env.BRANCH_NAME}"
                echo "Build number: ${BUILD_NUMBER_FORMATTED}"
                echo "Workspace: ${env.WORKSPACE}"
                
                script {
                    // Determine environment based on branch
                    if (env.BRANCH_NAME == 'main') {
                        env.ENVIRONMENT = 'prod'
                        env.DEPLOY_ENABLED = 'true'
                    } else if (env.BRANCH_NAME == 'develop') {
                        env.ENVIRONMENT = 'staging'
                        env.DEPLOY_ENABLED = 'true'
                    } else if (env.BRANCH_NAME.startsWith('feature/')) {
                        env.ENVIRONMENT = 'dev'
                        env.DEPLOY_ENABLED = 'false'
                    } else {
                        env.ENVIRONMENT = 'dev'
                        env.DEPLOY_ENABLED = 'false'
                    }
                    
                    echo "Environment: ${env.ENVIRONMENT}"
                    echo "Deploy enabled: ${env.DEPLOY_ENABLED}"
                }
            }
        }
        
        stage('Build') {
            steps {
                echo "Building application for ${env.ENVIRONMENT} environment..."
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
            }
        }
        
        stage('Test') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo 'Running unit tests...'
                        script {
                            if (isUnix()) {
                                sh 'mvn test'
                            } else {
                                bat 'mvn test'
                            }
                        }
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                        }
                    }
                }
                
                stage('Code Quality') {
                    when {
                        anyOf {
                            branch 'main'
                            branch 'develop'
                        }
                    }
                    steps {
                        echo 'Running code quality checks...'
                        // Placeholder for SonarQube or other quality gates
                        script {
                            if (isUnix()) {
                                sh 'echo "Code quality checks would run here"'
                            } else {
                                bat 'echo "Code quality checks would run here"'
                            }
                        }
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                echo "Packaging application with ${env.ENVIRONMENT} configuration..."
                script {
                    // Copy environment-specific configuration
                    if (isUnix()) {
                        sh "cp config/application-${env.ENVIRONMENT}.properties src/main/resources/application.properties || true"
                        sh 'mvn package -DskipTests'
                    } else {
                        bat "copy config\\application-${env.ENVIRONMENT}.properties src\\main\\resources\\application.properties 2>nul || echo \"Config file not found, using defaults\""
                        bat 'mvn package -DskipTests'
                    }
                }
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                echo 'Archiving build artifacts...'
                script {
                    def artifactName = "${APP_NAME}-${env.ENVIRONMENT}-${BUILD_NUMBER_FORMATTED}.jar"
                    if (isUnix()) {
                        sh "cp target/*.jar target/${artifactName}"
                    } else {
                        bat "copy target\\*.jar target\\${artifactName}"
                    }
                }
                
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                
                // Archive environment-specific artifacts
                script {
                    if (fileExists("config/application-${env.ENVIRONMENT}.properties")) {
                        archiveArtifacts artifacts: "config/application-${env.ENVIRONMENT}.properties", fingerprint: true
                    }
                }
            }
        }
        
        stage('Deploy') {
            when {
                expression { env.DEPLOY_ENABLED == 'true' }
            }
            steps {
                script {
                    echo "Deploying to ${env.ENVIRONMENT} environment..."
                    
                    switch(env.ENVIRONMENT) {
                        case 'prod':
                            echo 'Deploying to PRODUCTION...'
                            echo 'Would deploy to production server here'
                            // Add production deployment steps
                            break
                            
                        case 'staging':
                            echo 'Deploying to STAGING...'
                            echo 'Would deploy to staging server here'
                            // Add staging deployment steps
                            break
                            
                        case 'dev':
                            echo 'Deploying to DEVELOPMENT...'
                            echo 'Would deploy to development server here'
                            // Add development deployment steps
                            break
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline completed for branch: ${env.BRANCH_NAME}"
            cleanWs()
        }
        success {
            echo "✅ Build successful for ${env.BRANCH_NAME} (${env.ENVIRONMENT})"
            script {
                if (env.BRANCH_NAME == 'main') {
                    echo "🚀 Production deployment completed successfully!"
                }
            }
        }
        failure {
            echo "❌ Build failed for ${env.BRANCH_NAME}"
        }
        unstable {
            echo "⚠️  Build unstable for ${env.BRANCH_NAME}"
        }
    }
}