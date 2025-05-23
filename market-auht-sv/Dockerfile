FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/market-auht-sv-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_market-auth-sv.jar
ENTRYPOINT ["java", "-jar", "app_market-auth-sv.jar"]