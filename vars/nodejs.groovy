def lintchecks(){
    sh "echo Performing lintckecks for $COMPONENT"
    sh "npm i jslint"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
}

def call(COMPONENT){
    pipeline { 
        agent {
            label 'ws'
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
                    sh "echo Performing static for $COMPONENT"  
                }
            }
       }
    }
}