package com.backbase.flow.sme.onboarding.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProcessConstants {

    public static final String DMN_OUTPUT_DBA_REQUIRED = "dbaRequired";
    public static final String DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED = "businessLicenseRequired";
    public static final String DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED = "businessStructureDocumentsRequired";
    public static final String DMN_OUTPUT_DOCUMENT_REQUIRED = "documentsRequired";

    public static final String DMN_DBA_REQUIRED = "dba-doc";
    public static final String DMN_BUSINESS_LICENCE_REQUIRED = "business-license";
    public static final String DMN_BUSINESS_STRUCTURE_REQUIRED = "biz-struc";
    public static final String DMN_DOCUMENT_REQUIRED = "required-docs";

    public static final String DMN_DECIDED_ON_BUSINESS_RELATION = "decide-on-bizrel";
    public static final String DMN_DECIDED_ON_BUSINESS_RELATION_BUSINESS_STRUCTURE = "businessStructure";
    public static final String DMN_DECIDED_ON_BUSINESS_RELATION_BUSINESS_SUBTYPE = "businessSubtype";
    public static final String DMN_DECIDED_ON_BUSINESS_RELATION_REQUIRED = "businessRelationRequired";

    public static final String BPM_KYB = "kyb";
    public static final String BPM_KYC = "kyc";
    public static final String BPM_APP_ONBOARDING = "app-onboarding";
    public static final String BPM_DATA_GATHERING = "data-gathering";
    public static final String BPM_AML_TYPE_BUSINESS = "business";
    public static final String BPM_AML_TYPE_PERSON = "person";

    public static final String DOCUMENT_TYPE_DBA = "DBA document";
    public static final String DOCUMENT_TYPE_BUSINESS_LICENSE = "Business License";
    public static final String DOCUMENT_TYPE_BUSINESS_STRUCTURE = "Business Structure";
    public static final String INITIAL_NOTE = "Initial Note";
    public static final String DOCUMENT_REFERENCE_ID = "Documents";

    public static final String PROCESS_VARIABLE_CASE_KEY = "caseKey";
}
