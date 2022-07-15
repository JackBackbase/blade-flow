package com.backbase.flow.sme.onboarding.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.service.companylookup.domain.CompanyLookupConstants;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo.Status;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import org.junit.jupiter.api.Test;

class KybProcessIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void startProcess_withPotentialDBA_returnReview() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessKnownName("potential_match").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        var caseData = caseDataService.getCaseByKey(exCase.getKey()).getCaseData(SmeCaseDefinition.class);

        var amlInfo = caseData.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo();

        assertNotNull(amlInfo);
        assertNotNull(amlInfo.getAmlResult());
        assertTrue(amlInfo.getReviewNeeded());
        assertFalse(amlInfo.getReviewApproved());
        assertEquals(Status.IN_REVIEW, amlInfo.getStatus());
    }

    @Test
    @WithFlowAnonymousUser
    void startProcess_withValidDBA_returnSuccess() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessKnownName("test").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        var caseData = caseDataService.getCaseByKey(exCase.getKey()).getCaseData(SmeCaseDefinition.class);
        var events = getEventsByName(exCase.getKey(), TestConstants.EVENT_NAME_KYB_COMPLETED);

        var amlInfo = caseData.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo();

        assertThat(events).isNotEmpty();
        assertNotNull(amlInfo);
        assertNotNull(amlInfo.getAmlResult());
        assertFalse(amlInfo.getReviewNeeded());
        assertTrue(amlInfo.getReviewApproved());
        assertEquals(Status.SUCCESS, amlInfo.getStatus());
    }

    @Test
    @WithFlowAnonymousUser
    void startProcess_withoutDBA_returnEvent() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        var caseData = caseDataService.getCaseByKey(exCase.getKey()).getCaseData(SmeCaseDefinition.class);
        var events = getEventsByName(exCase.getKey(), TestConstants.EVENT_NAME_KYB_NOT_REQUIRED);

        assertThat(events).isNotEmpty();
        assertNull(caseData.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo());
    }

    @Test
    @WithFlowAnonymousUser
    void startProcess_withSolPropAndWithoutSubtype_returnEvent() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        // TODO real event should be checked here after adding the business relation process
        var events = getEventsByName(exCase.getKey(), TestConstants.EVENT_NAME_KYB_COMPLETED);

        assertThat(events).isNotEmpty();
    }

    @Test
    @WithFlowAnonymousUser
    void startProcess_withPartnershipAndGeneral_returnEvent() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().partnership().generalSubtype()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        // TODO real event should be checked here after adding the business relation process
        var events = getEventsByName(exCase.getKey(), "NotRequiredBusinessRelationEvent");

        assertThat(events).isNotEmpty();
    }

    @Test
    @WithFlowAnonymousUser
    void startProcess_withPartnershipAndNotGeneral_returnEvent() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().partnership().jointVentureSubtype()
            .firstName("John").lastName("Doe").email("john@email.invalid").isRegistrar(true)
            .businessLegalName("legal").businessOperationState("AR").build());

        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYB);

        completeCompanyLookupUserTask(exCase.getKey().toString());

        var events = getEventsByName(exCase.getKey(), "BusinessRelationPendingEvent");

        assertThat(events).isNotEmpty();
    }

    private void completeCompanyLookupUserTask(String caseKey) {
        completeUserTask(caseKey, CompanyLookupConstants.PROCESS_DEFINITION_KEY, CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID);
    }
}
