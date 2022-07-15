package com.backbase.flow.onboarding.us.interaction.resume;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.cases.CaseFilterRequest;
import com.backbase.flow.casedata.definition.CaseDefinitionId;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.resume.casefinder.CaseFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoApplicantAwareCaseFinder implements CaseFinder {

    public static final String MAIN_APPLICANT_PHONE_NUMBER_INDEX_NAME = "mainApplicant.phoneNumber";
    public static final String MAIN_APPLICANT_EMAIL_INDEX_NAME = "mainApplicant.email";
    public static final String CO_APPLICANT_PHONE_NUMBER_INDEX_NAME = "coApplicant.phoneNumber";
    public static final String CO_APPLICANT_EMAIL_INDEX_NAME = "coApplicant.email";
    public static final String DENIED_FRAUD_DOCUMENT_STATUS = "DENIED_FRAUD";
    public static final String DENIED_UNSUPPORTED_ID_COUNTRY_DOCUMENT_STATUS = "DENIED_UNSUPPORTED_ID_COUNTRY";
    public static final String FAIL_REVIEW_RESULT = "fail";

    @Value("${definitions.casedata.schema}")
    private final String caseDefinition;
    private final CaseDataService caseDataService;
    private final JourneyMapper<Onboarding> journeyMapper;
    private final ObjectMapper objectMapper;

    private CaseDefinitionId caseDefinitionId;

    @PostConstruct
    void init() {
        caseDefinitionId = CaseDefinitionId.fromString(caseDefinition);
    }

    @Override
    public Optional<Case> findPreviousCase(Map<String, Object> caseData, UUID caseKeyToIgnore) {
        final Onboarding onboarding = objectMapper.convertValue(caseData, Onboarding.class);

        Optional<Case> foundCase = Optional.empty();

        if (!OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            return Optional.empty();
        }

        final Applicant applicant = onboarding.getMainApplicant();
        if (Boolean.TRUE.equals(applicant.getPhoneNumberVerified())) {
            foundCase = search(List.of(
                MAIN_APPLICANT_PHONE_NUMBER_INDEX_NAME,
                CO_APPLICANT_PHONE_NUMBER_INDEX_NAME),
                applicant.getPhoneNumber(), caseKeyToIgnore);
        } else if (Boolean.TRUE.equals(applicant.getEmailVerified())) {
            foundCase = search(List.of(
                MAIN_APPLICANT_EMAIL_INDEX_NAME,
                CO_APPLICANT_EMAIL_INDEX_NAME),
                applicant.getEmail(), caseKeyToIgnore);
        }

        if (foundCase.isPresent()) {
            final Onboarding read = journeyMapper.read(null, null, foundCase.get());
            if (isAmlIdvValidToResume(OnboardingCaseDataUtils.getApplicant(read))) {
                return foundCase;
            }
        }

        return Optional.empty();
    }

    private Optional<Case> search(Collection<String> fieldNames, String fieldValue, UUID uuidToIgnore) {
        return fieldNames.stream()
            .flatMap(fieldName -> search(fieldName, fieldValue, uuidToIgnore))
            .max(Comparator.comparing(Case::getLastModifiedDate));
    }

    private Stream<Case> search(String fieldName, String fieldValue, UUID uuidToIgnore) {
        final CaseFilterRequest caseFilterRequest = new CaseFilterRequest()
            .setPropertyName(fieldName)
            .setPropertyValues(Collections.singletonList(fieldValue))
            .setIncludeArchived(false);

        return caseDataService.filterCasesByCaseDefinitionId(caseDefinitionId, caseFilterRequest)
            .stream()
            .filter(c -> !c.getKey().equals(uuidToIgnore));
    }

    private boolean isAmlIdvValidToResume(Applicant applicant) {
        return !isIdvDenied(applicant) && !isIdvReviewFail(applicant) && !isAmlFailed(applicant);
    }

    private boolean isIdvDenied(Applicant applicant) {
        return applicant.getIdentityVerificationResult() != null
            && (DENIED_FRAUD_DOCUMENT_STATUS
            .equals(applicant.getIdentityVerificationResult().getDocumentStatus())
            || DENIED_UNSUPPORTED_ID_COUNTRY_DOCUMENT_STATUS
            .equals(applicant.getIdentityVerificationResult().getDocumentStatus()));
    }

    private boolean isIdvReviewFail(Applicant applicant) {
        return applicant.getIdentityVerificationResult() != null
            && FAIL_REVIEW_RESULT.equals(applicant.getIdentityVerificationResult().getManualReviewResult());
    }

    private boolean isAmlFailed(Applicant applicant) {
        return applicant.getAntiMoneyLaunderingInfo() != null
            && Status.FAILED.equals(applicant.getAntiMoneyLaunderingInfo().getStatus());
    }

}
