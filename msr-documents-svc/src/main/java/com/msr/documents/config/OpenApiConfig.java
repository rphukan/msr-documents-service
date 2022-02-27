package com.msr.documents.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Created by ranjan on 25/10/20.
 */
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(authorizationCode = @OAuthFlow(
        authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
        scopes = {@OAuthScope(name = "lending", description = "lending app")}))
)
public class OpenApiConfig {

}
