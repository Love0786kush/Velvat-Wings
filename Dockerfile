FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8099

CMD ["java", "-jar", "target/ecommerce-0.0.1-SNAPSHOT.jar"]
