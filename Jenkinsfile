pipeline {
    agent any
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    stages{
        stage("Cleanup Workspace"){
                steps {
                cleanWs()
                }
        }
        stage("Checkout from SCM"){
                steps {
                    git branch: 'master', credentialsId: 'gitHub', url: 'https://github.com/tayadepss/local-cicd.git'
                }
        }
        stage("Build Application"){
            steps {
                sh "mvn clean package"
            }

       }
 	stage("Test Application"){
           steps {
                 sh "mvn test"
           }
       }

       stage("SonarQube Analysis"){
           steps {
	           script {
		        withSonarQubeEnv(credentialsId: 'sonarqube-jenkins-token') { 
                        sh "mvn sonar:sonar -Dsonar.host.url=http://172.31.0.3:9000"
		        }
	           }	
           }
       }
     stage("Quality Gate"){
           steps {
               script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube-jenkins-token'
                }	
            }

        }
    
    }  
    post {
	    failure {
             emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
             subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Failed", 
             mimeType: 'text/html',to: "bharatiy1988@gmail.com"
        }
        success {
            emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
            subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Successful", 
            mimeType: 'text/html',to: "bharatiy1988@gmail.com"
        }  
        cleanup {
            /* clean up our workspace */
            deleteDir()
            /* clean up tmp directory */
            dir("${workspace}@tmp") {
                deleteDir()
            }
            /* clean up script directory */
            dir("${workspace}@script") {
                deleteDir()
            }
        }
    }
}
