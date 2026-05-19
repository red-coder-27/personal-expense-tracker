# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY . .

# Build the project using Maven
RUN ./mvnw clean package -DskipTests

# Copy the JAR file into the container
COPY target/personal-expense-tracker.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]