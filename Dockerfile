
FROM maven:3.8.2-jdk-11 AS build-env
WORKDIR /app

LABEL maintainer="krlsedu@gmail.com"

COPY pom.xml ./
RUN mvn dependency:go-offline
RUN mvn spring-javaformat:help

COPY . ./
RUN mvn spring-javaformat:apply
RUN mvn package -DskipTests

FROM openjdk:11-jdk-oracle
WORKDIR /app

COPY --from=build-env /app/target/MonitorConsumo-0.0.1-SNAPSHOT.jar MonitorConsumo.jar
ENTRYPOINT ["java","-jar","MonitorConsumo.jar"]