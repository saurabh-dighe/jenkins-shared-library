def sonarchecks(){
    sh "echo Sonar checks started for $COMPONENT"
    sh "sonar-scanner -Dsonar.host.url=http://172.31.19.197:9000 -Dsonar.login=admin -Dsonar.password=password -Dsonar.projectKey=$COMPONENT $ARGS"
    sh "echo Sonar checks started for $COMPONENT"
}