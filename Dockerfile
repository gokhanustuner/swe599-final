FROM openjdk:11-jdk-slim
# VOLUME /tmp
# RUN apt-get update
# RUN apt-get install -y maven
# RUN ./mvnw install
# RUN ./mvnw package
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mdm.jar
ENTRYPOINT ["java", "-jar", "mdm.jar"]