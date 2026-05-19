# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the source code and pom.xml
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application on Tomcat
FROM tomcat:10-jdk17

# Remove the default ROOT application
RUN rm -rf "$CATALINA_HOME/webapps/ROOT"

# Copy the built WAR file from the builder stage
COPY --from=builder /app/target/expense-tracker-1.0.0.war "$CATALINA_HOME/webapps/ROOT.war"

# Expose the port Tomcat runs on
EXPOSE 8080

# The CMD is inherited from the Tomcat base image