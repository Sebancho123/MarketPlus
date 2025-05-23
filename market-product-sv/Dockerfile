FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/market-product-sv-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_market-product-sv.jar
ENTRYPOINT ["java", "-jar", "app_market-product-sv.jar"]