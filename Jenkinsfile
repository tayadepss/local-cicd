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
    
    
    }  
}
