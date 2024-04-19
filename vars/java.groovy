def lintchecks(){
    sh "echo performing Lint Checks for $COMPONENT"
    // sh "mvn checkstyle:check|| true"
}

def call(COMPONENT){
    pipeline { 
        agent {
            label 'ws'
        }
        environment{
            NEXES_URL= "172.31.19.197"
            SONAR_CRED= credentials('NEXES_CRED')
        }
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintchecks()
                    }
                }
            }
            stage('Compiling java code'){
                steps {
                    sh "mvn clean compile"
                    sh "ls -ltr target/"
                }
            }            
            stage('Static Code Analysis'){
                steps {
                    script {
                        env.ARGS = "-Dsonar.java.binaries=target/"
                        common.sonarchecks()
                    }
                }
            }
            stage('Get sonar result'){
                steps {
                    script {
                        common.sonarrusult()
                    }
                }
            }
            stage('testing'){
                steps{
                    sh "echo testing"         
                }
            }            
       }
    }
}