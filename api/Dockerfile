### Build
FROM maven:3-openjdk-8-slim as build

COPY pom.xml /build/
COPY src /build/src

WORKDIR /build

RUN mvn clean package

### RUN
FROM openjdk:jre-alpine

COPY --from=build /build/target/blikjesteller-api-thorntail.jar /opt/blikjesteller-api-thorntail.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/opt/blikjesteller-api-thorntail.jar", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv4Addresses=true"]
