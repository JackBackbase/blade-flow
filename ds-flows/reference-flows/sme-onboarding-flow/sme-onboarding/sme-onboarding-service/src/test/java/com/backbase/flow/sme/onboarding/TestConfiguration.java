package com.backbase.flow.sme.onboarding;

import static org.mockito.Mockito.mock;

import com.backbase.flow.sme.onboarding.credential.CredentialService;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class TestConfiguration {

    @Bean
    @Primary
    CredentialService mockCredentialService() {
        return mock(CredentialService.class);
    }

    @Bean
    Keycloak mockKeycloak() {
        return mock(Keycloak.class);
    }
}
