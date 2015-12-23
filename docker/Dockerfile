FROM tomcat:8.0.30-jre8

RUN rm -rf /usr/local/tomcat/webapps/*
ADD blikjesteller-1.0.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]