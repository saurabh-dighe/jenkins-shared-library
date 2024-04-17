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
       }
    }
}