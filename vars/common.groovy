def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    sh "sonar-scanner -Dsonar.host.url=http://$NEXES_URL:9000 -Dsonar.login=admin -Dsonar.password=password -Dsonar.projectKey=$COMPONENT $ARGS"
    sh "echo Sonar checks started for $COMPONENT"
}