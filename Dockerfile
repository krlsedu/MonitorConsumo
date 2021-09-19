FROM openjdk:11-jdk-oracle
MAINTAINER krlsedu.com
COPY target/MonitorConsumo-0.0.1-SNAPSHOT.jar MonitorConsumo.jar
ENTRYPOINT ["java","-jar","/MonitorConsumo.jar"]