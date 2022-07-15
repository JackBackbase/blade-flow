package com.backbase.flow.onboarding.us.helpers.it;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import java.util.UUID;

public class InBranchAssertions extends BaseAssertions {

    public static void assertSubmitPersonalInformation(InteractionResponseDto response, Applicant applicant) {
        assertSubmitPersonalInfo(response, applicant, SUBMIT_IN_BRANCH);
    }

    public static void assertProductSelectionList(InteractionResponseDto response) {
        assertProductSelectionList(response, SELECT_PRODUCTS_STEP);
    }

    public static void assertSelectProducts(InteractionResponseDto response, Onboarding onboarding) {
        assertSelectProducts(response, onboarding, PERSONAL_INFO);
    }

    public static void assertSubmitInBranch(InteractionResponseDto response, String caseKey) {
        assertSubmitInBranch(response, caseKey, SUCCESSFULLY_DONE_STEP);
    }

}
