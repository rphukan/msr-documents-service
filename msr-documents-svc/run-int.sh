export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=

java -jar ./target/msr-documents-svc-0.0.1-SNAPSHOT.jar \
--spring.profiles.active=int \
--com.msr.eureka.url="https://localhost:8181/discovery/eureka" \
--com.msr.configserver.enabled=true
