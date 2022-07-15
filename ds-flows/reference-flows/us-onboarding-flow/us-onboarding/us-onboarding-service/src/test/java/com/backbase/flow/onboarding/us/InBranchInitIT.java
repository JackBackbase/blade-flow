package com.backbase.flow.onboarding.us;

import static com.backbase.flow.onboarding.us.helpers.PersonalInfoStepCalls.submitPersonalInformation;
import static com.backbase.flow.onboarding.us.helpers.ProductSelectionListStepCalls.getProductList;
import static com.backbase.flow.onboarding.us.helpers.SelectProductsStepCalls.selectProducts;
import static com.backbase.flow.onboarding.us.helpers.SubmitInBranchStepCalls.submitInBranch;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertCaseStatus;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.getCaseData;
import static com.backbase.flow.onboarding.us.helpers.it.InBranchAssertions.assertProductSelectionList;
import static com.backbase.flow.onboarding.us.helpers.it.InBranchAssertions.assertSelectProducts;
import static com.backbase.flow.onboarding.us.helpers.it.InBranchAssertions.assertSubmitInBranch;
import static com.backbase.flow.onboarding.us.helpers.it.InBranchAssertions.assertSubmitPersonalInformation;

import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InBranchInitIT extends BaseIT {

    private static final String FIRST_NAME_MOCK_KYC_VALIDATION_OK = "John";
    private static final String AGE_18_DOB = LocalDate.now().minusYears(18).toString();


    @BeforeEach
    public void beforeEach() {
        setInteraction("in-branch-onboarding");
    }

    @Test
    void happyFlow() throws Exception {

        final InteractionResponseDto productSelectionListResponse = getProductList(null);
        assertProductSelectionList(productSelectionListResponse);

        final String interactionId = productSelectionListResponse.getInteractionId();

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto personalInformationResponse = submitPersonalInformation(
            FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB, interactionId);
        assertSubmitPersonalInformation(personalInformationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitInBranch = submitInBranch(interactionId);
        assertSubmitInBranch(submitInBranch, caseKey);

        assertCaseStatus(caseKey, "Primary case status", "open", true);
    }
}
