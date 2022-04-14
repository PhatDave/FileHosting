FROM openjdk:11-jre
COPY target/filehosting-1.0.jar restapi.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","restapi.jar"]