package com.msr.documents.config;

import com.mongodb.client.MongoClient;
import com.msr.documents.exception.TenantIdMissingException;
import com.msr.security.MsrSecurityConstants;
import com.msr.security.PreAuthenticatedAuthenticationDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Created by ranjan on 10/11/20.
 */
@Slf4j
public class TenantAwareMongoTemplate extends MongoTemplate {

  public TenantAwareMongoTemplate(MongoClient mongoClient, String databaseName) {
    super(mongoClient, databaseName);
  }

  @Override
  public String getCollectionName(Class<?> entityClass) {
    String tenantId = this.getTenantId();
    String collectionName = super.getCollectionName(entityClass);
    if (StringUtils.isNotBlank(tenantId)) {
      collectionName = tenantId.concat("_").concat(collectionName);
    } else {
      log.error("missing tenant id.");
      throw new TenantIdMissingException("Could to find tenantId in the request.");
    }
    log.info("working on collection >>>> {}", collectionName);
    return collectionName;
  }

  private String getTenantId() {
    String tenantId = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication;
      tenantId = (String) principal.getTokenAttributes().get(MsrSecurityConstants.TENANT_ID);
      log.info("tenant id >>>> {}", tenantId);
    } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
      tenantId = ((PreAuthenticatedAuthenticationDetails) authentication
          .getDetails()).getTenantId();
      log.info("tenant id >>>> {}", tenantId);
    }
    return tenantId;
  }

}
