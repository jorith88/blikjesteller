FROM jboss/wildfly:10.1.0.Final

ADD customization /opt/jboss/wildfly/customization/
ADD modules /opt/jboss/wildfly/modules/

RUN /opt/jboss/wildfly/customization/execute.sh

# Fix for Error: Could not rename /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

ADD blikjesteller.war /opt/jboss/wildfly/standalone/deployments/
