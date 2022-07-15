package com.backbase.flow.sme.onboarding.credential;

import org.springframework.stereotype.Service;

@Service
public interface CredentialService {

    boolean exists(String email);

}
