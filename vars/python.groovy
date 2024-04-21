def lintchecks(){
    sh "echo performing Lint Checks $COMPONENT"
    // sh "pip3.6 install pylint"
    // sh "ptlint *.py|| true"
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
                steps{
                    script{
                        common.testcases()
                    }
                }
            }
       }
    }
}