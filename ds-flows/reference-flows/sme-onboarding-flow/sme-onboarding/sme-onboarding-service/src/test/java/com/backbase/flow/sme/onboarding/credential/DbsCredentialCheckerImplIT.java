package com.backbase.flow.sme.onboarding.credential;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.config.CustomerConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;

class DbsCredentialCheckerImplIT extends BaseIntegrationIT {

    private final String emailAddress = "tsmit@email.invalid";
    private DbsCredentialCheckerImpl credentialService;

    @Autowired
    Keycloak keycloak;

    @Autowired
    CustomerConfigurationProperties customerConfigurationProperties;

    @BeforeEach
    void init() {
        credentialService = new DbsCredentialCheckerImpl(keycloak, customerConfigurationProperties);
    }

    @Test
    void credential_whenExists_shouldReturnTrue() {
        var realmResource = mock(RealmResource.class);
        var usersResource = mock(UsersResource.class);

        doReturn(realmResource).when(keycloak).realm(any());
        doReturn(usersResource).when(realmResource).users();
        doReturn(1).when(usersResource)
            .count(any(), any(), any(), any());

        assertTrue(credentialService.exists(emailAddress));
    }

    @Test
    void credential_whenNotExists_shouldReturnFalse() {
        var realmResource = mock(RealmResource.class);
        var usersResource = mock(UsersResource.class);

        doReturn(realmResource).when(keycloak).realm(any());
        doReturn(usersResource).when(realmResource).users();
        doReturn(0).when(usersResource)
            .count(any(), any(), any(), any());

        assertFalse(credentialService.exists(emailAddress));
    }
}
