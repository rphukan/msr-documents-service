export AWS_ACCESS_KEY_ID=AKIA2JZFZWJMTB7KA3OB
export AWS_SECRET_ACCESS_KEY=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

java -jar ./target/msr-documents-svc-0.0.1-SNAPSHOT.jar \
--spring.profiles.active=int \
--com.msr.eureka.url="https://localhost:8181/discovery/eureka" \
--com.msr.configserver.enabled=true
