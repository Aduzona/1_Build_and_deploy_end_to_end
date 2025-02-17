FROM eclipse-temurin:22-jre
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar