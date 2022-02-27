package com.msr.documents.config;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by ranjan on 8/10/20.
 */
@Slf4j
@Configuration
@EnableMongoAuditing
public class AuditConfig {

  @Bean
  public AuditorAware<String> audit() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (null != context && null != context.getAuthentication()) {
      String user = context.getAuthentication().getName();
      log.info("User name : {} has been resolved for auditing", user);
      return () -> Optional.of(user);
    }
    log.info("No username found from security context, setting default as system for auditing");
    return () -> Optional.of("system");
  }

}
