package com.backbase.flow.onboarding.us.interaction.dto.response;

import lombok.Data;

@Data
public class CredentialsResponseDto {

    private String firstName;
    private Boolean isJointAccount;
    private Boolean isMainApplicantFlow;

}
