# Builder
#FROM maven:3.5-jdk-8 as builder
#COPY . /
#RUN /usr/local/bin/mvn-entrypoint.sh mvn package

# Final app
FROM openjdk:8
COPY target/*.jar /app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]