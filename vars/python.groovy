def lintchecks(){
    sh "echo performing Lint Checks"
    // sh "pip3.6 install pylint"
    // sh "ptlint *.py|| true"
}

def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    
    sh "echo Sonar checks started for $COMPONENT"
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
                    script {
                        sonarchecks()
                    }
                }
            }
       }
    }
}