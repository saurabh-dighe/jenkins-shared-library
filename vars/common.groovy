def lint-checks(){
    stage('Lint Checks'){
        sh "echo performing Lint Checks for $COMPONENT"
    }
}

def sonar-checks(){
    stage('Static Code Analysis'){
        sh "echo Sonar checks started for $COMPONENT"
        //sh "sonar-scanner -Dsonar.host.url=http://$NEXES_URL:9000 -Dsonar.login=$SONAR_CRED_USR -Dsonar.password=$SONAR_CRED_PSW -Dsonar.projectKey=$COMPONENT $ARGS"
        sh "echo Sonar checks completd for $COMPONENT"
    }
}

def sonarrusult(){
    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate >> gate.sh"
    // sh "bash gate.sh $SONAR_CRED_USR $SONAR_CRED_PSW $NEXES_URL $COMPONENT"
    sh "echo sonar scan completed"
}

def test-cases(){
    stage('Running Test Cases'){
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

def artifacts(){
    stage('Checking Artifacts on Nexus'){
        env.upload_status = sh(returnStdout: true, script: "curl -s -L http://172.31.22.7:8081/service/rest/repository/browse/$COMPONENT/ | grep $COMOPENT-$TAG_NAME || true")
        print upload_status
    }

    if(env.upload_status == ""){
        stage('Prepairing Artifacts'){
            if(env.APPTYPE == "nodejs"){
                sh 'echo prepairing artifacts'
                sh 'npm install'
                sh 'zip -r $COMPONENT-$TAG_NAME.zip node_modules server.js'
            }
            else if(env.APPTYPE == "java"){
                sh 'echo prepairing artifacts'
                sh 'mvn clean install'
                sh 'zip -r $COMPONENT-$TAG_NAME.zip node_modules server.js systemd.service'
            }
            else if(env.APPTYPE == "python"){
                sh 'echo prepairing artifacts'
                sh 'pip3 install'
                sh 'zip -r $COMPONENT-$TAG_NAME.zip node_modules server.js'
            }
        }
        stage('Publishing Artifacts'){
            sh 'echo Publishing artifacts'
            sh 'curl -f -v -u admin:password --upload-file $COMPONENT-$TAG_NAME.zip http://172.31.22.7:8081/repository/$COMPONENT/$COMPONENT-$TAG_NAME.zip'
        }
    } 
    else{
        sh "echo Arifact $COMOPENT-$TAG_NAME already present"
    }   
}
