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
                    sh "echo Performing static code analysis for $COMPONENT"  
                }
            }
       }
    }
}