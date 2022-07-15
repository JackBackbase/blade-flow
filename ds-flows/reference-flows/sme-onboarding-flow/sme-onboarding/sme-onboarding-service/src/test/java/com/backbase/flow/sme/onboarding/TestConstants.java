package com.backbase.flow.sme.onboarding;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public final static String CASE_DEFINITION_ID = "sme";
    public static final String COMPANY_LOOKUP_IDT = "company-lookup-idt";
    public static final String IDENTITY_VERIFICATION_IDT = "idv-idt";
    public static final String BUSINESS_RELATIONS_IDT = "business-relations-idt";
    public final static String IN_BRANCH_ONBOARDING_START = "in-branch-onboarding-start";
    public final static String REQUIRED_DOCS = "required-docs";

    public final static String AML_REVIEW_ACTIVITY = "AML_Review_Activity";
    public final static String AML_PROCESS_ID = "aml";
    public final static String MANUAL_REVIEW = "manual-review";
    public final static String UPLOAD_DOCUMENTS_TASK = "uploadDocumentsTask";
    public final static String REVIEW_DOCUMENTS_TASK = "reviewDocumentsTask";
    public final static String RISK_ASSESSMENT_PROCESS_ID = "risk-assessment";
    public final static String REVIEW_APPLICATION = "Review_application";
    public final static String BUSINESS_RELATION_PROCESS_ID = "br-process";
    public static final String BUSINESS_RELATION_INPUT_OWNER_INFORMATION_TASK_ID =
        "Activity_BR_Input_Owner_Information";
    public static final String BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID = "Activity_BR_Review_Information";
    public static final String BUSINESS_RELATION_PENDING_EVENT = "BusinessRelationPendingEvent";
    public static final String BUSINESS_RELATION_IN_REVIEW_EVENT = "BusinessRelationInReviewEvent";
    public static final String BUSINESS_RELATION_COMPLETE_EVENT = "BusinessRelationCompleteEvent";
    public static final String COMPANY_LOOKUP_PENDING_EVENT = "CompanyLookupPendingEvent";
    public static final String COMPANY_LOOKUP_COMPLETE_EVENT = "CompanyLookupCompleteEvent";
    public final static String APPLICANT_ONBOARDING_PROCESS_ID = "app-onboarding";

    public static final String TRANSPORTATION_AND_WAREHOUSING_GENERAL = "48";
    public static final String FINANCE_AND_INSURANCE = "52";
    public static final String MINING_QUARRYING_OIL_AND_GAS_EXTRACTION = "21";

    public final static String ACTION_URL =
        "/client-api/interaction/v2/interactions/{interactionName}/actions/{actionName}";
    public static final String FILE_URL =
        "/client-api/interaction/v2/interactions/{interactionName}/files/{actionName}";

    public final static String ADDRESS_VALIDATION_IDT = "address-validation-idt";
    public final static String APPLICATION_CENTER_INIT = "application-center-init";
    public final static String CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS =
        "check-business-relation-and-document-requests-conditions";
    public final static String COMPLETE_TASK = "complete-task";
    public final static String DOCUMENT_REQUEST_JOURNEY = "document-request-journey";
    public final static String DONE = "done";
    public final static String EXISTING_CASE = "existing-case";
    public final static String FETCH_OTP_EMAIL = "fetch-otp-email";
    public final static String GET_MILESTONES = "get-milestones";
    public final static String GET_PRODUCT_LIST = "get-product-list";
    public final static String IDENTITY_VERIFICATION_INITIATION = "identity-verification-initiation";
    public final static String IDENTITY_VERIFICATION_RESULT = "identity-verification-result";
    public final static String LOAD_DOCUMENT_REQUESTS = "load-document-requests";
    public final static String POTENTIAL_MATCH = "potential_match";
    public final static String SELECT_PRODUCTS = "select-products";
    public final static String SUBMIT_SSN = "submit-ssn";
    public final static String SUBMIT_ADDRESS = "submit-address";
    public final static String OTP_VERIFICATION = "otp-verification";
    public final static String REQUEST_OTP = "request-otp";
    public final static String RELATION_TYPE = "relation-type";
    public final static String SELECT_RELATION_TYPE = "select-relation-type";
    public final static String SEND_REGISTRAR_DETAILS = "send-registrar-details";
    public final static String SME_ONBOARDING = "sme-onboarding";
    public final static String UPDATE_CURRENT_USER_OWNER_STEP = "update-current-user-owner-step";
    public final static String GET_BUSINESS_ROLES = "get-business-roles";
    public final static String GET_BUSINESS_PERSONS = "get-business-persons";
    public final static String UPDATE_CURRENT_USER_OWNER = "update-current-user-owner";
    public final static String UPDATE_OWNER = "update-owner";

    public final static String COMPLETE_BUSINESS_OWNER_STEP = "complete-business-owners-step";
    public final static String SELECT_CONTROL_PERSON = "select-control-person";
    public final static String COMPLETE_SUMMARY_STEP = "complete-summary-step";
    public final static String BUSINESS_OWNERS = "business-owners";
    public final static String CONTROL_PERSONS = "control-persons";
    public final static String BUSINESS_SUMMARY = "business-summary";
    public final static String SME_ONBOARDING_ANCHOR = "sme-onboarding-anchor";
    public final static String SME_ONBOARDING_INIT = "sme-onboarding-init";
    public final static String SME_ONBOARDING_ANCHOR_DATA = "sme-onboarding-anchor-data";
    public final static String SME_ONBOARDING_CHECK_CASE_EXIST = "sme-onboarding-check-case-exist";
    public final static String SME_ONBOARDING_CHECK_CASE = "sme-onboarding-check-case";
    public final static String SME_ONBOARDING_SSN = "sme-onboarding-ssn";
    public final static String SME_ONBOARDING_PERSONAL_ADDRESS = "sme-onboarding-personal-address";
    public final static String SME_ONBOARDING_LANDING = "sme-onboarding-landing";
    public final static String SME_ONBOARDING_LANDING_DATA = "sme-onboarding-landing-data";
    public final static String SME_APPLICATION_CENTER = "sme-application-center";
    public final static String SUBMIT_DOCUMENT_REQUESTS = "submit-document-requests";
    public final static String VERIFY_OTP = "verify-otp";
    public final static String CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST =
        "check-business-relation-and-document-request";
    public static final String FIRST_NAME_POTENTIAL_MATCH = "potentialMatch";

    public final static String APPLICANT_ONBOARDING_PENDING_EVENT = "ApplicantOnboardingPendingEvent";
    public final static String APPLICANT_ONBOARDING_FINISHED_EVENT = "ApplicantOnboardingFinishedEvent";
    public final static String AML_PENDING_EVENT = "AmlPendingEvent";
    public final static String AML_SUCCEED_EVENT = "AmlSucceedEvent";
    public final static String APPLICANT_ACTION_NOT_REQUIRED_EVENT = "ApplicantActionNotRequiredEvent";
    public final static String KYC_PENDING = "KYCPending";
    public final static String KYC_COMPLETED = "KYCCompleted";

    public final static String EVENT_NAME_KYB_NOT_REQUIRED = "KYBNotRequired";
    public final static String EVENT_NAME_KYB_COMPLETED = "KYBComplete";


}
