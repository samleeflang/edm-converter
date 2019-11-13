# Build stage
FROM maven:3.6.0-jdk-11-slim AS build

COPY src ./src
COPY libs ./libs
COPY pom.xml .
RUN mvn --quiet -e -f ./pom.xml clean package

# Run stage
FROM openjdk:11-jre-slim

COPY --from=build ./target .

CMD ["java", "-jar", "cc-lod-edmconverter-0.0.1-SNAPSHOT.jar"]