FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
# Copy resources
COPY src/main/resources/ config/
# Copy jar file
COPY ${JAR_FILE} spring-gateway.jar
ENTRYPOINT ["java","-jar","spring-gateway.jar"]