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
            stage('Performing testing'){
                steps{
                    script{
                        common.testcases()
                    }
                }
            }
            stage('Making artifacts'){
                when{
                    expression {env.TAG_NAME != null}
                }
                steps{
                    sh 'echo prepairing artifacts'
                    sh 'npm clean-install'
                    sh 'zip -r $COMPONENT.zip node_modules server.js'
                }
            }
            stage('Publishing artifacts'){
                when{
                    expression {env.TAG_NAME != null}
                }
                steps{
                    sh 'echo Publishing artifacts'
                }
            } 
       }
    }
}