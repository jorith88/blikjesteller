FROM openjdk:jre-alpine

ADD target/blikjesteller-api-swarm.jar /opt/blikjesteller-api-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/opt/blikjesteller-api-swarm.jar", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv4Addresses=true"]