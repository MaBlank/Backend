# Bauphase
FROM maven:3.8.4-openjdk-17-slim as build
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests

# Ausführungsphase
FROM openjdk:17
COPY --from=build /app/target/Backend-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
