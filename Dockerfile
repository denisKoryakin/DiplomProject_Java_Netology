FROM openjdk:17-jdk-alpine

EXPOSE 8080

ADD target/DiplomProject-0.0.1-SNAPSHOT.jar cloud_app.jar

CMD ["java", "-jar", "cloud_app.jar"]