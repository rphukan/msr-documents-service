# documents-service
## msr-documents-parent
The `parent pom` for the maven projects.

## msr-documents-common-jar
The common interface for the different implementations of the documents-svc.

## msr-documents-mongo-jar
The `MongoDB` based implementation of the `documents-svc` where the documents are stored in a `Mongo document database`.

## msr-documents-s3-jar
The `AWS S3` based implementation of the `documents-svc` where the documents are stored in `AWS S3`. Note that `documents-svc` still uses `MongoDB` for storing the document's information where as the actual docuement is stored in S3.

## msr-documents-svc
There are different `maven profiles` for building the `documents-svc`. The `aws profile` builds a `AWS S3` implemantation of the `documents-svc` and the `mongo profile (default)` builds a `MongoDB` based implemantation of the `documents-svc`. Use the correct profile like below
```
mvn clean install -P mongodb
```

# Docker Build
The project has a `Docker` file for building the image. The below commands can be used to build the `docker image` and `push` to `docker hub`
```
docker build --tag msrfintech/documents-svc:<tag> .
docker push msrfintech/documents-svc:<tag>
```

# Docker Run
First create a user deifned `bridge` network if not already available and connect all your containers to the same network. Once connected to the same user-defined network, containers can communicate with each other using container IP addresses or container names. Refer the [docker docs on networking](https://docs.docker.com/network/) for more details.
```
docker network list
docker network create msr
```

## `local` profile
It will create the required database `docstore` and the collections if not present already.
```
docker run --detach --name documents-svc --network msr --publish 8184:8184 --env "spring.profiles.active=local" --env "com.msr.eureka.url=https://discovery-svc:8181/eureka" --env "com.msr.configserver.enabled=false" --env "mongodb.url=mongodb://admin:password@mongodb:27017/" --env "com.msr.keycloak.url=https://msrkeycloak:8443/auth" msrfintech/documents-svc
```
The `Swagger` docs can be accessed on https://localhost:8184/documents-service/swagger-ui.html