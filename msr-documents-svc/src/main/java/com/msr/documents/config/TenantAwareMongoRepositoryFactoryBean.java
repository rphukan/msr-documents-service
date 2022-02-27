package com.msr.documents.config;

import com.msr.documents.exception.TenantIdMissingException;
import com.msr.security.MsrSecurityConstants;
import com.msr.security.PreAuthenticatedAuthenticationDetails;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Created by ranjan on 19/7/20.
 */
@Slf4j
public class TenantAwareMongoRepositoryFactoryBean extends MongoRepositoryFactoryBean {

  public TenantAwareMongoRepositoryFactoryBean(Class repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {

    return new MongoRepositoryFactory(operations) {

      @Override
      public <T, ID> MongoEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

        MongoPersistentEntity entity = operations.getConverter().getMappingContext()
            .getPersistentEntity(domainClass);

        return new MappingMongoEntityInformation<T, ID>(entity) {
          @Override
          public String getCollectionName() {
            String collectionName = entity.getCollection();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken) {
              JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication;
              String tenantId = (String) principal.getTokenAttributes()
                  .get(MsrSecurityConstants.TENANT_ID);
              if (StringUtils.isBlank(tenantId)) {
                log.error("missing tenant id.");
                throw new TenantIdMissingException("Could to find tenantId in the request.");
              }
              String customCollectionName = tenantId.concat("_").concat(collectionName);
              log.info("tenant specific collectionName for collection {} is {}", collectionName,
                  customCollectionName);
              return customCollectionName;
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
              String tenantId = ((PreAuthenticatedAuthenticationDetails) authentication
                  .getDetails()).getTenantId();
              if (StringUtils.isBlank(tenantId)) {
                log.error("missing tenant id.");
                throw new TenantIdMissingException("Could to find tenantId in the request.");
              }
              String customCollectionName = tenantId.concat("_").concat(collectionName);
              log.info("tenant specific collectionName for collection {} is {}", collectionName,
                  customCollectionName);
              return customCollectionName;
            } else {
              log.info(
                  "JwtAuthenticationToken not found, tenant specific collectionName for collection {} could not be computed.",
                  collectionName);
              return entity.getCollection();
            }
          }
        };
      }

      @Override // you should not need this when DATAMONGO-2297 is resolved
      protected Object getTargetRepository(RepositoryInformation information) {
        MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(
            information.getDomainType());
        return getTargetRepositoryViaReflection(information, entityInformation, operations);
      }
    };
  }

}
