def lintchecks(){
    sh "echo performing Lint Checks"
    // sh "pip3.6 install pylint"
    // sh "ptlint *.py|| true"
}

def call(){
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