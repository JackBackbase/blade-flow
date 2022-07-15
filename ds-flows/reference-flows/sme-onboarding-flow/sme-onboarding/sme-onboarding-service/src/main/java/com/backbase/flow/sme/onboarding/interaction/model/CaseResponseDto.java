package com.backbase.flow.sme.onboarding.interaction.model;

import lombok.Data;

@Data
public class CaseResponseDto {

    private boolean caseExist;
    private String caseKey;
    private boolean identityCredentialExist;
    private String firstName;
    private String lastName;
}
