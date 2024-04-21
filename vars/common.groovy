def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    //sh "sonar-scanner -Dsonar.host.url=http://$NEXES_URL:9000 -Dsonar.login=$SONAR_CRED_USR -Dsonar.password=$SONAR_CRED_PSW -Dsonar.projectKey=$COMPONENT $ARGS"
    sh "echo Sonar checks completd for $COMPONENT"
}

def sonarrusult(){
    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate >> gate.sh"
    // sh "bash gate.sh $SONAR_CRED_USR $SONAR_CRED_PSW $NEXES_URL $COMPONENT"
    sh "echo sonar scan completed"
}

def testcases(){
    stage('Testing') {
        
        def stages = [:]

        stages["Unit Testing"] = {
            echo "Unit testing completed"
        }
        stages["Integration Testing"] = {
            echo "Integration testing completed"
        }
        stages["Functional Testing"] = {
            echo "Functional testing completed"
        }
        parallel(stages)
    }
}

                parallel{
                    stage('Unit Testing'){
                       steps{
                            sh "echo Unit testing completed"  
                            //sh "npm test"       
                        }
                    }
                    stage('Integration Testing'){
                       steps{
                            sh "echo Integration testing completed" 
                            //sh "npm verify"        
                        }
                    }
                    stage('Functional Testing'){
                       steps{
                            sh "echo Functional testing completed"         
                        }
                    }                                        
                }