def lintchecks(){
    sh "echo Performing lintckecks for $COMPONENT"
    // sh "npm i jslint"
    // sh "node_modules/jslint/bin/jslint.js server.js || true"
}
def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    sh "sonar-scanner -Dsonar.host.url=http://172.31.19.197:9000 -Dsonar.login=admin -Dsonar.password=password -Dsonar.projectKey=$COMPONENT $ARGS"
    sh "echo Sonar checks started for $COMPONENT"
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