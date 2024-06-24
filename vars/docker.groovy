def call(){
    node{
        git branch: 'main', url: "https://github.com/saurabh-dighe/${COMPONENT}.git"
        // common.lintchecks()
        // env.ARGS = "-Dsonar.java.binaries=target/"
        // common.sonarchecks()
        // common.testcases()
        if(env.TAG_NAME != null)
        {
            stage('Generate Docker Image'){
                if(env.APPTYPE == "nodejs"){
                    sh "npm install"
                }
                else if(env.APPTYPE == "maven"){
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip  ${COMPONENT}.jar systemd.service"
                }
                else if(env.APPTYPE == "python"){
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                }
                else if(env.APPTYPE == "angular"){
                    sh '''
                        cd static/
                        zip ../${COMPONENT}-${TAG_NAME}.zip *
                    '''
                }
            }
            stage('Publish Docker Image') {
                sh '''
                    wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
                    aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 992382386864.dkr.ecr.us-east-1.amazonaws.com
                    docker build -t 992382386864.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} .
                    docker push 992382386864.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}
                '''
            }
        }
    }
}

