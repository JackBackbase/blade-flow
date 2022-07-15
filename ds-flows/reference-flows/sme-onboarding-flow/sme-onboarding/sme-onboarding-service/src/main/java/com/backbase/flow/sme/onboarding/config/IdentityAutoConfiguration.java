package com.backbase.flow.sme.onboarding.config;

import com.backbase.flow.sme.onboarding.credential.CredentialService;
import com.backbase.flow.sme.onboarding.credential.DbsCredentialCheckerImpl;
import com.backbase.flow.sme.onboarding.credential.DefaultCredentialCheckerImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "backbase.sme-onboarding.credentials", value = "impl", havingValue = "default", matchIfMissing = true)
    public CredentialService defaultCredentialService() {
        return new DefaultCredentialCheckerImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.sme-onboarding.credentials", value = "impl", havingValue = "DBS")
    public CredentialService dbsCredentialService(
        Keycloak keycloak, CustomerConfigurationProperties customerConfigurationProperties
    ) {
        return new DbsCredentialCheckerImpl(keycloak, customerConfigurationProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.identity", value = "enabled", havingValue = "true")
    public Keycloak keycloak(IdentityConfigurationProperties identityConfigurationProperties) {
        return KeycloakBuilder.builder()
            .clientId(identityConfigurationProperties.getClientId())
            .serverUrl(identityConfigurationProperties.getServerUrl())
            .grantType(OAuth2Constants.PASSWORD)
            .realm(identityConfigurationProperties.getMasterRealm())
            .username(identityConfigurationProperties.getUsername())
            .password(identityConfigurationProperties.getPassword())
            .build();
    }

}
