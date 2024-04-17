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