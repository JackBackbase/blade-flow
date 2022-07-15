package com.backbase.flow.sme.onboarding.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CaseDefinitionConstants {

    public static final String CASE_DEFINITION_KEY = "sme";
    public static final String EMAIL_INDEX = "email";
    public static final String BUSINESS_ADDRESS_CONTEXT = "AddressJourneyBusiness";
    public static final String PERSONAL_ADDRESS_CONTEXT = "AddressJourneyPersonal";
    public static final String OTP_JOURNEY_EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String DOCUMENT_REQUEST = "documentRequest";
    public static final String DOCUMENT_TYPE = "documentType";
    public static final String GROUP_ID = "groupId";
    public static final String APPLICATION_SUBMITTED_EVENT = "ApplicationSubmittedEvent";
    public static final String APPLICANT_ID = "applicantId";

}
