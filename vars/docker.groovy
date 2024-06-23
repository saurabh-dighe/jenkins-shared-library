def call(){
    node{
        git branch: 'main', url: "https://github.com/saurabh-dighe/${COMPONENT}.git"
        // common.lintchecks()
        // env.ARGS = "-Dsonar.java.binaries=target/"
        // common.sonarchecks()
        // common.testcases()
        if(env.TAG_NAME != null)
        {
            stage('Checking Artifacts on Nexus'){
        env.upload_status = sh(returnStdout: true, script: "curl -s -L http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print upload_status
    }

    if(env.upload_status == ""){
        stage('Generate Docker Image'){
            if(env.APPTYPE == "nodejs"){
                sh "echo prepairing artifacts"
                sh "npm install"
            }
            else if(env.APPTYPE == "maven"){
                sh "echo Preparing Artifacts"
                sh "mvn clean package"
                sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip  ${COMPONENT}.jar systemd.service"
            }
            else if(env.APPTYPE == "python"){
                sh "echo preparing artifacts"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
            }
            else if(env.APPTYPE == "angular"){
                sh '''
                    echo preparing artifacts
                    cd static/
                    zip ../${COMPONENT}-${TAG_NAME}.zip *
                '''
            }
        }
        stage('Publish Docker Image') {
                docker build -t 992382386864.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} .
                docker push 992382386864.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}
            }
        }
    }
        }
    }
}

