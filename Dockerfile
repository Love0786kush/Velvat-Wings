# Build Stage
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# only copy ecommerce folder (where pom.xml exists)
COPY ecommerce ./ecommerce
WORKDIR /app/ecommerce

RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/ecommerce/target/*.jar app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]
