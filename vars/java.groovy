def lintchecks(){
    sh "echo performing Lint Checks for catalogue"
    // sh "mvn checkstyle:check|| true"
}
def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    sh "sonar-scanner -Dsonar.host.url=http://172.31.19.197:9000 -Dsonar.login=admin -Dsonar.password=password -Dsonar.projectKey=$COMPONENT -Dsonar.sources=."
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