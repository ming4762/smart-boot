FROM openjdk:17-oracle
VOLUME /tmp
ADD smart-samples/smart-sample-system/target/smart-sample-system-3.0.0-SNAPSHOT.jar app.jar
EXPOSE 9095
ENTRYPOINT ["java", "-jar", "/app.jar"]
