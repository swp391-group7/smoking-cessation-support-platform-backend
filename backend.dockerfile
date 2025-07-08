FROM maven:4-openjdk-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM container-registry.oracle.com/java/jdk:24
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

