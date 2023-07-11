FROM openjdk:17
VOLUME /tmp
ADD target/BackendFachpraktikumRefactored-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
