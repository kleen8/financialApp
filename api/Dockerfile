# Backend build stage (Maven with OpenJDK)
FROM maven:3.8.4-openjdk-17 AS backend-builder

WORKDIR /app
# only copy the needed files
COPY . /app
# Copy the pom.xml (from the root) and install dependencies
COPY pom.xml .

# RUN mvn dependency:go-offline

# Package the app
RUN mvn clean install

# Runtime stage with JDK 17 (no Maven needed here)
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built .jar file from the backend-builder stage
COPY --from=backend-builder /app/api/target/api-1.0-SNAPSHOT.jar /app/backend.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/backend.jar"]

