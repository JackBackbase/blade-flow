package com.backbase.flow.sme.onboarding.credential;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DefaultCredentialCheckerImplIT {

    @Test
    void credential_whenExists_shouldReturnTrue() {
        var credentialService = new DefaultCredentialCheckerImpl();
        assertTrue(credentialService.exists("tsmit@email.invalid"));
    }
}
