def lintchecks(){
    sh "echo performing Lint Checks for $COMPONENT"
    // sh "mvn checkstyle:check|| true"
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
            stage('Static Code Analysis') {
                steps {
                    sh "echo performing static checks"
                }
            }
        }
    }
}