# Build stage using Maven
FROM maven:3.9.6-eclipse-temurin-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage using Oracle JDK 24
FROM container-registry.oracle.com/java/jdk:24
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
