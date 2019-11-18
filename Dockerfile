# Run stage
FROM openjdk:11-jre-slim

COPY ./target .

CMD ["java", "-jar", "cc-lod-edmconverter-0.0.1-SNAPSHOT.jar"]