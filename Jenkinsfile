#!groovy
env.RELEASE_COMMIT = "1"
env.VERSION_NAME = ""

pipeline {
    agent none
    stages {
        stage('CheckBranch') {
            agent any
            steps {
                script {
                    result = sh(script: "git log -1 | grep 'Triggered Build'", returnStatus: true)
                    echo 'result ' + result
                    env.RELEASE_COMMIT = result == 0 ? '0' : '1'
                }
            }
        }
        stage('Build') {
            agent any
            tools {
                maven 'M3'
            }
            when {
                expression { env.RELEASE_COMMIT != '0' }
            }
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Gerar versão') {
            agent any
            tools {
                maven 'M3'
            }
            when {
                expression { env.RELEASE_COMMIT != '0' }
            }
            steps {
                script {
                    echo 'RELEASE_COMMIT ' + env.RELEASE_COMMIT
                    if (env.BRANCH_NAME == 'master') {
                        echo 'Master'
                        VERSION = VersionNumber(versionNumberString: '${BUILD_DATE_FORMATTED, "yy"}.${BUILD_WEEK,XX}.${BUILDS_THIS_WEEK,XXX}')
                        sh 'mvn versions:set versions:commit -DnewVersion=RELEASE'
                        sh 'mvn clean install package -DskipTests'
                    } else {
                        echo 'Dev'
                        VERSION = VersionNumber(versionNumberString: '${BUILD_DATE_FORMATTED, "yyyyMMdd"}.${BUILDS_TODAY}.${BUILD_NUMBER}')
                        VERSION = VERSION + '-SNAPSHOT'
                    }

                    withCredentials([usernamePassword(credentialsId: 'gitHub', passwordVariable: 'password', usernameVariable: 'user')]) {
                        script {

                            echo "Creating a new tag"
                            sh 'git pull https://krlsedu:${password}@github.com/krlsedu/MonitorConsumo.git HEAD:' + env.BRANCH_NAME
                            sh 'mvn versions:set versions:commit -DnewVersion=' + VERSION
                            sh 'mvn clean install package -DskipTests'
                            sh "git add ."
                            sh "git config --global user.email 'krlsedu@gmail.com'"
                            sh "git config --global user.name 'Carlos Eduardo Duarte Schwalm'"
                            sh "git commit -m 'Triggered Build: " + VERSION + "'"
                            sh 'git push https://krlsedu:${password}@github.com/krlsedu/MonitorConsumo.git HEAD:' + env.BRANCH_NAME
                        }
                    }
                    env.VERSION_NAME = VERSION
                }
            }
        }
        stage('Docker image') {
            agent any
            when {
                expression { env.RELEASE_COMMIT != '0' }
            }
            steps {
                sh "docker build -t krlsedu/monitor-consumo:latest -t krlsedu/monitor-consumo:${env.VERSION_NAME} ."
            }
        }

        stage('Docker Push') {
            agent any
            when {
                expression { env.RELEASE_COMMIT != '0' }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                    sh 'docker push krlsedu/monitor-consumo'
                    sh 'docker push krlsedu/monitor-consumo:' + env.VERSION_NAME
                }
            }
        }

        stage('Service update') {
            agent any
            when {
                expression { env.RELEASE_COMMIT != '0' }
                branch 'master'
            }
            steps {
                sh 'docker service update --image krlsedu/monitor-consumo:' + env.VERSION_NAME + ' monitor_consumo_services_monitor'
            }
        }
    }
}
