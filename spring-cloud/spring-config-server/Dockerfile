FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
#WORKDIR /app
COPY ${JAR_FILE} spring-config.jar
COPY config config
ENTRYPOINT ["java","-jar", "spring-config.jar"]