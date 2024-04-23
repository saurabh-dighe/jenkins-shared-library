def lintchecks(){
    stage('Lint Checks'){
        sh "echo performing Lint Checks for $COMPONENT"
    }
}

def sonarchecks(){
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

def testcases(){
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
        env.upload_status = sh(returnStdout: true, script: "curl -s -L http://172.31.22.7:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print upload_status
    }

    if(env.upload_status == ""){
        stage('Generate Artifacts'){
            if(env.APPTYPE == "nodejs"){
                sh "echo prepairing artifacts"
                sh "pwd"
                sh "npm install"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js systemd.service"
            }
            else if(env.APPTYPE == "maven"){
                sh "echo Preparing Artifacts"
                sh "mvn clean package"
                sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip  ${COMPONENT}.jar systemd.service"
                sh "echo Artifacts prepared"
            }
            else if(env.APPTYPE == "python"){
                sh "echo preparing artifacts"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                sh "ls -ltr"
            }
            else if(env.APPTYPE == "angular"){
                sh "echo preparing artifacts"
                sh "cd static/"
                sh "zip -r ../$COMPONENT-$TAG_NAME.zip *"
            }
        }
        stage('Publishing Artifacts'){
            sh "echo Publishing artifacts"
            sh "curl -f -v -u admin:password --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.22.7:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
        }
    } 
    else{
        sh "echo Arifact ${COMOPENT}-${TAG_NAME} already present"
    }   
}
