FROM openjdk:jre-alpine

ADD target/blikjesteller-api-thorntail.jar /opt/blikjesteller-api-thorntail.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/opt/blikjesteller-api-thorntail.jar", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv4Addresses=true"]
