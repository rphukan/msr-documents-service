package com.msr.documents.config;

import com.msr.security.AbstractWebJwtSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Created by ranjan on 29/5/20.
 */
@Configuration
@EnableWebSecurity
public class WebJwtSecurityConfig extends AbstractWebJwtSecurityConfig {

  @Override
  public void configure(HttpSecurity http) throws Exception {

    //setup the resource server configurations
    super.configure(http);

    http.csrf().disable().cors().disable();
    //TODO - do we need CRSF ?? getting some error in POST from Swagger

  }
}
