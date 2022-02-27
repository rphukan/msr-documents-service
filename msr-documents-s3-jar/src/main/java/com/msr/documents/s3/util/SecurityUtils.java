package com.msr.documents.s3.util;

import com.msr.security.MsrSecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Created by ranjan on 7/11/20.
 */
@Component("securityUtils")
public class SecurityUtils {

  public static String getTenantId() {
    String tenantId = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication;
      tenantId = (String) principal.getTokenAttributes().get(MsrSecurityConstants.TENANT_ID);
    }
    return tenantId;
  }
}
