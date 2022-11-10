#Run first 'mvn package spring-boot:repackage' to generate the jar file and copy file in Dockerfile folder

FROM openjdk:11
COPY springboot-db-1.0-SNAPSHOT.jar db.jar
ENTRYPOINT ["java","-jar","db.jar"]
EXPOSE 8082
