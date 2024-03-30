# Stage 1: Build the Spring Boot application with Maven
FROM maven:3.8.4-openjdk-8 AS builder

WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download the project dependencies
RUN mvn -B dependency:go-offline

# Copy the application source code
COPY src ./src

# Compile and package the application
RUN mvn -B package

# Stage 2: Create the final image with the compiled Spring Boot application
FROM openjdk:8-jre-alpine

WORKDIR /app

# Copy the compiled JAR file from the builder stage
COPY --from=builder /app/target/github-repository-service-1.0.0.jar ./github-repository-service-1.0.0.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "github-repository-service-1.0.0.jar"]