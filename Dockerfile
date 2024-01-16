FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY pom.xml /opt/app

ARG JAR_FILE=target/monitor-consumo.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dfile.encoding=UTF-8 -XX:+UseSerialGC","-jar","app.jar"]
