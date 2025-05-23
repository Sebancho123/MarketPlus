FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/market-api-gateway-sv-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_market-api-gateway-sv.jar
ENTRYPOINT ["java", "-jar", "app_market-api-gateway-sv.jar"]
