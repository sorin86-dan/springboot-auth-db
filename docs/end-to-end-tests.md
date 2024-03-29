**[Back](../README.md)**

# Running automated end to end tests locally

To run the end to end tests you need do change all references to Docker containers:
- comment **db.ip.address** in *springboot-auth/src/main/resources/application.properties*;
- comment **redis.ip.address** in *springboot-db/src/main/resources/application.properties*;
- comment lines in *end-to-end-tests/src/test/resources/application.properties*.

After this run **AuthMainApp.java**, **DBMainApp.java** and start Redis server (makes sure that it runs on default values: **localhost:6379**).

Now you can run tests from **DefaultTest.java** (placed in *end-to-end-tests* folder).

# Running automated end to end tests using Docker

These automated tests will run on microservices **springboot-auth** and **springboot-db**; with these microservices running through Docker containers.

## Create docker image with sprinboot-auth

In the folder *springboot-auth* run:
```shell script
mvn clean install -DskipTests
mvn package spring-boot:repackage -DskipTests
cp target/springboot-auth-1.0.0-SNAPSHOT.jar src/main/resources/
docker build -t auth src/main/resources
```

## Create docker image with sprinboot-db

In the folder *springboot-db* run:
```shell script
mvn clean install -DskipTests
mvn package spring-boot:repackage -DskipTests
cp target/springboot-db-1.0.0-SNAPSHOT.jar src/main/resources/
docker build -t db src/main/resources
```

## Run docker containers and the automated tests

In the folder *end-to-end-tests* run:
```shell script
mvn clean install -DskipTests
docker network create --subnet=172.0.0.0/16 microservices-grid
docker run -d -p 8081:8081 --net microservices-grid --ip 172.0.0.2 auth
docker run -d -p 8082:8082 --net microservices-grid --ip 172.0.0.3 db
docker run -d -p 6379:6379 --net microservices-grid --ip 172.0.0.4 redis
mvn clean test
```
or
```shell script
mvn clean install -DskipTests
cd src/test/resources
docker-compose up -d
mvn clean test
```

### Push docker images to DockerHub

Next run the following command to push docker images to DockerHub (after creating the repository **sorindan86/docker-sorin**):
```shell script
docker login
docker images
docker tag <image-id> sorindan86/docker-sorin:auth
docker tag <image-id> sorindan86/docker-sorin:db
docker tag <image-id> sorindan86/docker-sorin:redis
docker push sorindan86/docker-sorin:auth
docker push sorindan86/docker-sorin:db
docker push sorindan86/docker-sorin:redis
```