package com.msr.documents.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.msr.security.ssl.ServiceSslContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by ranjan on 16/5/20.
 */
@Data
@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = {"com.msr.documents"},
    repositoryFactoryBeanClass = TenantAwareMongoRepositoryFactoryBean.class)
public class MongoConfig {

  @Value("${msr.mongodb.database:docstore}")
  private String database;

  @Value("${msr.mongodb.connectionString}")
  private String connectionString;

  @Value("${msr.mongodb.sslEnabled}")
  private boolean sslEnabled;

  @Autowired
  private ServiceSslContext serviceSslContext;

  @Bean
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(this.connectionString);
    log.info("connectionString.getSslEnabled() = {}", connectionString.getSslEnabled());
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .applyToSslSettings(sslSettingsBuilder -> {
          sslSettingsBuilder.enabled(sslEnabled);
          sslSettingsBuilder.invalidHostNameAllowed(true);
          sslSettingsBuilder.context(this.serviceSslContext.getSslContext());
        })
        .build();
    MongoClient mongoClient = MongoClients.create(settings);
    return mongoClient;
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new TenantAwareMongoTemplate(this.mongoClient(), this.database);
  }
}
