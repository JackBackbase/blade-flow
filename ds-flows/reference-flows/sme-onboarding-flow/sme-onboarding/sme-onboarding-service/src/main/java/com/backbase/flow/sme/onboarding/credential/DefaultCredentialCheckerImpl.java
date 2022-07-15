package com.backbase.flow.sme.onboarding.credential;

public class DefaultCredentialCheckerImpl implements CredentialService {

    @Override
    public boolean exists(String email) {
        return true;
    }
}
