# Use latest jboss/base-jdk:8 image as the base
FROM jboss/base-jdk:8

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 19.0.0.Final
ENV JBOSS_HOME /opt/jboss/wildfly
ENV MDMS_ENV dev

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
RUN cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz

RUN ls -la

# Copy all the SQL drivers, property files and MDMS java war file to the image
COPY MDMSJavaFiles /opt/jboss/wildfly/standalone/deployments
RUN ls -la /opt/jboss/wildfly/standalone/deployments

COPY WFProperties  /opt/jboss/wildfly/standalone/configuration

RUN chown -R jboss:0 ${JBOSS_HOME}
RUN chmod -R g+rw ${JBOSS_HOME}
RUN ls -la /opt/jboss/wildfly/standalone/configuration

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose MDMS Java Web App
EXPOSE 8080
# Expose WF Management Console
EXPOSE 9990

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Denv=${MDMS_ENV}"]

# Build the image
 docker build -t icetech/mdms .

# --hostname=mdms \

# Run the container
  docker run \
  --name=mdms \
  -p 8080:8080 \
  -p 9990:9990 \
  --add-host=nes-sql:192.1.0.185 \
  --add-host=kamstrup-app01:192.1.0.181 \
  --add-host=ami1:192.1.0.210 \
  --add-host=ice-util01.spsi.co.za:10.30.20.49 \
  --add-host=dboratst.spsi.co.za:172.16.1.76 \
  --add-host=dboradev.spsi.co.za:172.16.1.73 \
  --add-host=dboramdms.spsi.co.za:172.16.1.87 \
  --add-host=db01.spsi.co.za:172.16.1.7 \
  --add-host=db02.spsi.co.za:172.16.1.53 \
  --add-host=dev.spsi.co.za:10.30.20.141 \
  -d icetech/mdms