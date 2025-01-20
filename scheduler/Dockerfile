# Build stage
FROM maven:3.8.4-openjdk-17 AS scheduler-builder

WORKDIR /app

# Copy project files
COPY . /app

# Build the scheduler module
RUN mvn clean install -pl scheduler -am

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR
COPY --from=scheduler-builder /app/scheduler/target/scheduler-1.0-SNAPSHOT.jar /app/scheduler.jar

# Create a logs directory
RUN chmod 777 /app/scheduler.jar

CMD ["java", "-jar", "/app/scheduler.jar"]

