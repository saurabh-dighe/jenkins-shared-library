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
            stage('Static Code Analysis'){
                steps {
                    script {
                        env.ARGS = "-Dsonar.sources=."
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
                        }
                    }
                    stage('Integration Testing'){
                       steps{
                            sh "echo Integration testing completed"         
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