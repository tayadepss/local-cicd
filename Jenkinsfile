pipeline {
    agent any
    tools {
        jdk 'Java21'
        maven 'Maven3'
	docker 'Docker'
    }
    environment {
	    APP_NAME = "local-cicd"
            RELEASE = "1.0.0"
            DOCKER_USER = "tayadepss"
            DOCKER_PASS = 'dockerHub'
            IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
            IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
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
                        sh "mvn sonar:sonar"
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
        stage("Build & Push Docker Image") {
            steps {
                script {
                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image = docker.build "${IMAGE_NAME}"
                    }

                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push('latest')
                    }
                }
            }
       }


	 stage("Trivy Scan") {
           steps {
               script {
	            sh ('docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image tayadepss/local-cicd:latest --no-progress --scanners vuln  --exit-code 0 --severity HIGH,CRITICAL --format table')
               }
           }
       }


       stage ('Cleanup Artifacts') {
           steps {
               script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
               }
          }
       }
       stage("Trigger CD Pipeline") {
            steps {
                script {
                    sh "curl -v -k --user Parmeshwar:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'ec2-43-204-229-139.ap-south-1.compute.amazonaws.com:8080/job/gitops-cicd-test-cd/buildWithParameters?token=gitops-token'"
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
