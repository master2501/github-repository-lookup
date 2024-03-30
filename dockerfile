# Use the official OpenJDK 8 image as a parent image
FROM openjdk:8-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/github-repository-service-1.0.0.jar /app/github-repository-lookup-1.0.0.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "github-repository-service-1.0.0.jar"]