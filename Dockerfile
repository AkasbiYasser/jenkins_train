# Use a lightweight base image with JRE to run the application
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY ./target/*.jar ./mvn-app.jar

# Expose the port on which your application listens
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "mvn-app.jar"]