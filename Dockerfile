# Use Tomcat 10 with OpenJDK 17 as the base image
FROM tomcat:10-jdk17-eclipse-temurin

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml into the container
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Run Maven to build the application
RUN ./mvnw clean package -DskipTests

# Copy the generated WAR file to the Tomcat webapps directory
COPY target/expense-tracker-1.0.0.war "$CATALINA_HOME/webapps/ROOT.war"

# Expose the port Tomcat runs on
EXPOSE 8080

# The CMD is inherited from the Tomcat base image