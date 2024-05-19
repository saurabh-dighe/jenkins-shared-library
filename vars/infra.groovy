def call(){
    properties([
        parameters([
            choice(name: 'ENV', choices: ['dev', 'prod'], description: 'Environment'),
            choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Action'),
            string(name: 'APP_VERSION', defaultValue: '000', description: 'Provide APP version'),
        ])
    ])
    node{
        git branch: 'main', url: "https://github.com/saurabh-dighe/${COMPONENT}.git"

        stage('Terraform Init'){
            sh '''
                cd mutable-infra
                terrafile -f ./env-${ENV}/Terrafile
                terraform init --backend-config=env-${ENV}/backend-${ENV}.tfvars
            '''
        }
        stage('Terraform Plan'){
            sh "cd mutable-infra"
            sh "terraform plan -var APP_VERSION=${APP_VERSION} --var-file env-${ENV}/${ENV}.tfvars"
        }
        stage("Terraform Action"){
            sh "cd mutable-infra"
            sh "terraform ${ACTION} -auto-approve -var APP_VERSION=${APP_VERSION} --var-file env-${ENV}/${ENV}.tfvars"
        }
    }
}
