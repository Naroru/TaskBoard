FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /
COPY pom.xml .
COPY /src /src
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:17-jdk-slim
COPY --from=builder /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
