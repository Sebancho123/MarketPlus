FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/market-sale-sv-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_market-sale-sv.jar
ENTRYPOINT ["java", "-jar", "app_market-sale-sv.jar"]