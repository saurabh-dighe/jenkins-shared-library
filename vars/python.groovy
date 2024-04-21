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
            stage('Performing testing'){
                steps{
                    script{
                        common.testcases()
                    }
                }
            }
            stage('Making artifacts'){
                // when{
                //     expression {env.TAG_NAME != null}
                // }
                steps{
                    sh 'echo prepairing artifacts'
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