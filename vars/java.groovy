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
            stage('Testing'){
                parallel{
                    stage('Unit Testing'){
                       steps{
                            sh "echo Unit testing completed"   
                            //sh "mvn test"      
                        }
                    }
                    stage('Integration Testing'){
                       steps{
                            sh "echo Integration testing completed" 
                            //sh "mvn verify"        
                        }
                    }
                    stage('Functional Testing'){
                       steps{
                            sh "echo Functional testing completed"         
                        }
                    }                                        
                }
            }            
       }
    }
}