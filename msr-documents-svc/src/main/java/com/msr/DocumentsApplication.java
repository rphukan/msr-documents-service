package com.msr;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableEurekaClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DocumentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentsApplication.class, args);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Document Management APIs")
            .version("1.0.0")
            .description(
                "APIs for documents management. You can change the actual document storage "
                    + "system by including the correct implementation jar. It "
                    + "can support storage systems like S3, simple mongodb etc. ")
            .contact(new Contact().name("Ranjan Phukan").email("ranjan.phukan@msrfintech.com")));
  }

}
