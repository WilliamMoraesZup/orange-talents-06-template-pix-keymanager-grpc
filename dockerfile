FROM openjdk:11-jdk-slim
MAINTAINER William Moraes
ARG JAR_FILE=build/libs/keymanager-0.1-all.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 50051

ENV APP_NAME keymanager-grpc


