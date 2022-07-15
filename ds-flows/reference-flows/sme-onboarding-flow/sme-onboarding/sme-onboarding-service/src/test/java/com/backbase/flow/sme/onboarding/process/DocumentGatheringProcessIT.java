package com.backbase.flow.sme.onboarding.process;

import static com.backbase.flow.sme.onboarding.TestConstants.FINANCE_AND_INSURANCE;
import static com.backbase.flow.sme.onboarding.TestConstants.MINING_QUARRYING_OIL_AND_GAS_EXTRACTION;
import static com.backbase.flow.sme.onboarding.TestConstants.TRANSPORTATION_AND_WAREHOUSING_GENERAL;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_BUSINESS_LICENCE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_BUSINESS_STRUCTURE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_DBA_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class DocumentGatheringProcessIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withLicenseRequiredAndKnownNameInTexas_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName("Test").businessOperationState("TX")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        var resultDocumentsRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultDocumentsRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultDocumentsRequired.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withLicenseRequiredAndKnownNameNotInTexas_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName("Test").businessOperationState("AK")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(result).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", false);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withoutLicenseRequiredAndKnownNameNotInTexas_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName("Test").businessOperationState("AK").businessIndustry(FINANCE_AND_INSURANCE).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", false);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withLicenseRequiredAndUnknownName_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName(null).businessOperationState("TX")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(result).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", false);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withLicenseRequiredAndKnownName_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName("Test").businessOperationState(null)
            .businessIndustry(MINING_QUARRYING_OIL_AND_GAS_EXTRACTION).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(result).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_wihLicenseRequiredAndUnknownName_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName(null).businessOperationState(null)
            .businessIndustry(MINING_QUARRYING_OIL_AND_GAS_EXTRACTION).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(result).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", false);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
    }


    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withoutLicenseRequiredAndUnknownName_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().soleProp()
            .businessKnownName(null).businessOperationState("AR").businessIndustry(FINANCE_AND_INSURANCE).build());

        var result = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
        assertThat(result).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(result.get(0)).containsEntry("documentsRequired", false);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", false);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", false);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withCorporationAndLicenseRequiredAndKnownNameInTexas_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().corporation()
            .businessKnownName("Test").businessOperationState("TX")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        var resultDocumentsRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());


        assertThat(resultDocumentsRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultDocumentsRequired.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", false);
    }

    @Test
    @WithFlowAnonymousUser
    void dmnCheck_withPartnershipAndSubTypeAndLicenseRequiredAndKnownNameInTexas_returnResult() {
        var exCase = startCase(SmeCaseDefBuilder.getInstance().partnership().generalSubtype()
            .businessKnownName("Test").businessOperationState("TX")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        var resultDocumentsRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            TestConstants.REQUIRED_DOCS, null, new HashMap<>());

        var resultBusinessLicenseRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var resultDbaRequired = this.flowDecisionService.checkDecisionTable(exCase.getKey(),
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var resultBusinessStructureRequired =
            this.flowDecisionService.checkDecisionTable(exCase.getKey(),
                DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());


        assertThat(resultDocumentsRequired).isNotEmpty();
        assertThat(resultBusinessStructureRequired).isNotEmpty();
        assertThat(resultBusinessLicenseRequired).isNotEmpty();
        assertThat(resultDbaRequired).isNotEmpty();
        assertThat(resultDocumentsRequired.get(0)).containsEntry("documentsRequired", true);
        assertThat(resultDbaRequired.get(0)).containsEntry("dbaRequired", true);
        assertThat(resultBusinessLicenseRequired.get(0)).containsEntry("businessLicenseRequired", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("businessStructureDocumentsRequired", true);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesOrganization", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("articlesIncorporation", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("operatingAgreement", false);
        assertThat(resultBusinessStructureRequired.get(0)).containsEntry("partnershipAgreement", true);
    }
}
