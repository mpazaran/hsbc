# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Update apt
RUN apt update

# Copy the pom.xml and download dependencies without building
COPY app/pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code to the container
COPY app/src /app/src

# Automatically recompile the code and redeploy the application
RUN apt install -y entr

# Copy the watch script
COPY watch.sh /app/watch.sh

# Make sure the script is executable
RUN chmod +x /app/watch.sh

# Package the application
RUN mvn package -DskipTests

# Stage 2: Create a lightweight image to run the application
FROM openjdk:17-slim

# Set the working directory for the runtime environment
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/restaurant-app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to start the watcher
#CMD ["/app/watch.sh"]

# Command to run the application
CMD ["java", "-jar", "/app/restaurant-app.jar"]

