#!groovy

pipeline {
  agent none
  stages {
    stage('Build') {
      agent any
        tools {
                maven 'M3'
            }
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }
    stage('Tests') {
      agent any
        tools {
                maven 'M3'
            }
      steps {
        sh 'mvn test'
      }
    }
    stage('Docker image') {
      agent any
      steps {
        sh 'docker build -t krlsedu/monitor-consumo:latest .'
      }
    }
    stage('Docker Push') {
      agent any
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
          sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'docker push krlsedu/monitor-consumo'
        }
      }
    }
    stage('Docker publish') {
	  agent any
	  steps {
		sh 'docker-compose -H tcp://192.168.15.85:2375 up -d'
	  }
	}
  }
}