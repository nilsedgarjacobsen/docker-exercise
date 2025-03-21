# Build stage
FROM maven:3.9-eclipse-temurin-21-jammy AS build

WORKDIR /app

# Kopiera pom.xml för caching av dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiera källkod och bygg
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Skapa en användare som applikationen kan köras som (säkerhetsåtgärd)
RUN addgroup --system spring && adduser --system --group spring

# Kopiera JAR-filen från build-steget
COPY --from=build /app/target/*.jar app.jar

# Ange spring-användaren som ägare och kör som denna användare
RUN chown spring:spring /app/app.jar
USER spring

# Exponera porten som Spring Boot-applikationen använder
EXPOSE 8080

# Kör applikationen
ENTRYPOINT ["java", "-jar", "app.jar"]