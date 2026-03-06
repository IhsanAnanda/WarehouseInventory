# --- STAGE 1: Build Stage ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 1. Copy only the pom.xml to download dependencies
# This allows Docker to cache your dependencies unless pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy your source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime Stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 3. Copy the compiled JAR from the build stage
# Spring Boot usually names the jar based on the artifactId and version
COPY --from=build /app/target/*.jar app.jar

# 4. Create a non-root user for better security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 5. Expose the port Spring Boot runs on
EXPOSE 8090

# 6. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]