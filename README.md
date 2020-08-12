# Running automated tests

These automated tests will run on microservices **springboot-auth** and **springboot-db**; with these microservices running through Docker containers.

## Create docker image with sprinboot-auth

In the folder *springboot-auth* run:
```shell script
mvn clean install
mvn package spring-boot:repackage
cp target/springboot-auth-1.0-SNAPSHOT.jar src/main/resources/
docker build -t auth src/main/resources
```

## Create docker image with sprinboot-db

In the folder *springboot-db* run:
```shell script
mvn clean install
mvn package spring-boot:repackage
cp target/springboot-db-1.0-SNAPSHOT.jar src/main/resources/
docker build -t db src/main/resources
```

### Run docker containers and the automated tests

In the folder *springboot-auth-db* run:
```shell script
mvn clean install -DskipTests
docker network create --subnet=172.0.0.0/16 microservices-grid
docker run -d -p 8081:8081 --net microservices-grid --ip 172.0.0.2 auth
docker run -d -p 8080:8080 --net microservices-grid --ip 172.0.0.3 db
mvn clean test
```