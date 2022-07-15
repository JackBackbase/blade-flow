package com.backbase.flow.sme.onboarding.credential;

import com.backbase.flow.sme.onboarding.config.CustomerConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;

@RequiredArgsConstructor
public class DbsCredentialCheckerImpl implements CredentialService {

    private final Keycloak keycloak;
    private final CustomerConfigurationProperties customerConfigurationProperties;

    @Override
    public boolean exists(String email) {
        var realm = keycloak.realm(customerConfigurationProperties.getExternalLegalEntity());
        return (long) realm.users()
            .count(null, null, email, null) > 0;
    }
}
