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
            NEXUS_CRED= credentials('NEXES_CRED')
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
            stage('Checking release'){
                when{
                    expression {env.TAG_NAME != null}
                }
                steps{
                    script{
                        print "checking releases"
                        #def upload_status =sh(returnStdout: true, script: "curl -s -L http://172.31.22.7:8081/service/rest/repository/browse/$COMPONENT/ | grep $TAG_NAME")
                        #print upload_status
                    }
                }
            }            
            stage('Making artifacts'){
                when{
                    expression {env.TAG_NAME != null}
                }
                steps{
                    sh 'echo prepairing artifacts'
                    sh 'npm install'
                    sh 'zip -r $COMPONENT-$TAG_NAME.zip node_modules server.js'
                }
            }
            stage('Publishing artifacts'){
                when{
                    expression {env.TAG_NAME != null}
                }
                steps{
                    sh 'echo Publishing artifacts'
                    sh 'curl -f -v -u admin:password --upload-file $COMPONENT-$TAG_NAME.zip http://172.31.22.7:8081/repository/$COMPONENT/$COMPONENT-$TAG_NAME.zip'
                }
            } 
       }
    }
}