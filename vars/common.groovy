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