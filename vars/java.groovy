def lintchecks(){
    sh "echo performing Lint Checks for $COMPONENT"
    // sh "mvn checkstyle:check|| true"
}
def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    sh "sonar-scanner -Dsonar.host.url=http://172.31.19.197:9000 -Dsonar.login=admin -Dsonar.password=password -Dsonar.projectKey=$COMPONENT -Dsonar.java.binaries=target/"
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
            stage('Compiling java code'){
                steps {
                    sh "mvn clean compile"
                    sh "ls -ltr target/"
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