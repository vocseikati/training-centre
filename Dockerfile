FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/vocseikati_masterwork-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} vocseikati_masterwork.jar
ENTRYPOINT ["java","-jar","vocseikati_masterwork.jar"]