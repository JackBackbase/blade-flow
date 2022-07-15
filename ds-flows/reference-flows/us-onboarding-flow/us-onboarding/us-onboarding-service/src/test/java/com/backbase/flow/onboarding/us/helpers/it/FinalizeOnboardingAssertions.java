package com.backbase.flow.onboarding.us.helpers.it;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;

public class FinalizeOnboardingAssertions extends BaseAssertions {

    public static void assertRequestOtp(InteractionResponseDto response) {
        assertRequestOtp(response, OTP_VERIFICATION_STEP);
    }

    public static void assertVerifyOtp(InteractionResponseDto response, Applicant applicant) {
        assertVerifyOtp(response, applicant, CREDENTIALS_STEP);
    }

    public static void assertSetupCredentials(InteractionResponseDto response, Applicant applicant,
        boolean isJointAccount, boolean isMainApplicantFlow) {
        final String expectedNextStep = isJointAccount && isMainApplicantFlow ? SUCCESSFULLY_DONE_CO_APPLICANT_STEP : SUCCESSFULLY_DONE_STEP;
        assertSetupCredentials(response, applicant, expectedNextStep);
    }

}