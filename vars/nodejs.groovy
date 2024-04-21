def lintchecks(){
    sh "echo Performing lintckecks for $COMPONENT"
    // sh "npm i jslint"
    // sh "node_modules/jslint/bin/jslint.js server.js || true"
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
                            //sh "npm test"       
                        }
                    }
                    stage('Integration Testing'){
                       steps{
                            sh "echo Integration testing completed" 
                            //sh "npm verify"        
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