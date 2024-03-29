# Start with a base image containing Java runtime
#FROM openjdk:8-jdk-alpine
FROM registry.cn-shanghai.aliyuncs.com/yuanbing/jdk8

# Add Maintainer Info
MAINTAINER Bing <yuanbing1113@gmail.com>

# Set Env
ENV TZ Asia/Shanghai

# The application's jar file
ARG JAR_FILE=target/stock-0.0.1-SNAPSHOT.jar

WORKDIR /upload-dir

WORKDIR /

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]