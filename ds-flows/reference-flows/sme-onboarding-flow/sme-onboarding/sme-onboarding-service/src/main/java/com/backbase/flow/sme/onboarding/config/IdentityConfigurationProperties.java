package com.backbase.flow.sme.onboarding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "backbase.identity")
public class IdentityConfigurationProperties {

    private String clientId;
    private String serverUrl;
    private String masterRealm;
    private String username;
    private String password;

}
