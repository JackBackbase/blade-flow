package com.backbase.flow.onboarding.us.interaction.resume;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.cases.CaseFilterRequest;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class CoApplicantAwareCaseFinderTest {

    public static final String IS_MAIN_APPLICANT_FLOW = "isMainApplicantFlow";
    private final String caseDefinition = "onboarding@1";
    private static final String MAIN_APPLICANT_FIELD = "mainApplicant";
    private static final String PHONE_NUMBER_FIELD = "phoneNumber";
    private static final String PHONE_NUMBER_VERIFIED_FIELD = "phoneNumberVerified";
    private static final String EMAIL_FIELD = "email";
    private static final String EMAIL_VERIFIED_FIELD = "emailVerified";

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private final JourneyReader<Onboarding> journeyReader = new OnboardingReader();
    private final JourneyMapper<Onboarding> journeyMapper = new JourneyMapper<>(Onboarding.class,
        List.of(journeyReader), List.of(), caseDataService);

    private final CoApplicantAwareCaseFinder caseFinder = new CoApplicantAwareCaseFinder(caseDefinition,
        caseDataService, journeyMapper, objectMapper);

    @Test
    void findPreviousCase_previousCaseWithEmailExists_caseIsReturned() {
        final Case sampleCase = validIdvAndAmlCase();
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(emailFilterRequest())))
            .thenReturn(List.of(sampleCase));
        setupCaseByKey(sampleCase);

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedEmailCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(sampleCase.getKey());
    }

    @Test
    void findPreviousCase_previousCaseWithPhoneNumberExists_caseIsReturned() {
        final Case sampleCase = validIdvAndAmlCase();
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberFilterRequest())))
            .thenReturn(List.of(sampleCase));
        setupCaseByKey(sampleCase);

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedPhoneNumberCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(sampleCase.getKey());
    }

    @Test
    void findPreviousCase_coApplicantIdExists_emptyOptionalReturned() {
        Optional<Case> previousCase = caseFinder
            .findPreviousCase(verifiedPhoneNumberCaseDataWithCoApplicantId(), UUID.randomUUID());

        verify(caseDataService, never()).filterCasesByCaseDefinitionId(any(), any());
        assertThat(previousCase).isEmpty();
    }

    @Test
    void findPreviousCase_coApplicantCaseExists_coApplicantCaseIsReturned() {
        final Case coApplicantCase = validIdvAndAmlCase();
        setupCaseByKey(coApplicantCase);

        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberFilterRequest())))
            .thenReturn(List.of());
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberCoApplicantFilterRequest())))
            .thenReturn(List.of(coApplicantCase));

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedPhoneNumberCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(coApplicantCase.getKey());
    }

    @Test
    void findPreviousCase_multipleCasesFound_latestIsReturned() {
        final Case oldestCase = validIdvAndAmlCase();
        oldestCase.setLastModifiedDate(oldestCase.getLastModifiedDate().minus(5, DAYS));
        final Case middleCase = validIdvAndAmlCase();
        middleCase.setLastModifiedDate(middleCase.getLastModifiedDate().minus(1, DAYS));
        final Case newestCase = validIdvAndAmlCase();
        setupCaseByKey(oldestCase, middleCase, newestCase);

        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(emailFilterRequest())))
            .thenReturn(List.of(middleCase, newestCase, oldestCase));

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedEmailCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(newestCase.getKey());
    }

    @Test
    void findPreviousCase_mainAndCoApplicantCasesExistsMainApplicantIsNewest_mainApplicantCaseIsReturned() {
        final Case mainApplicantCase = validIdvAndAmlCase();
        final Case coApplicantCase = validIdvAndAmlCase();
        coApplicantCase.setLastModifiedDate(coApplicantCase.getLastModifiedDate().minus(1, DAYS));
        setupCaseByKey(mainApplicantCase, coApplicantCase);

        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberFilterRequest())))
            .thenReturn(List.of(mainApplicantCase));
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberCoApplicantFilterRequest())))
            .thenReturn(List.of(coApplicantCase));

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedPhoneNumberCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(mainApplicantCase.getKey());
    }

    @Test
    void findPreviousCase_mainAndCoApplicantCasesExistsCoApplicantIsNewest_coApplicantCaseIsReturned() {
        final Case mainApplicantCase = validIdvAndAmlCase();
        mainApplicantCase.setLastModifiedDate(mainApplicantCase.getLastModifiedDate().minus(1, DAYS));
        final Case coApplicantCase = validIdvAndAmlCase();
        setupCaseByKey(mainApplicantCase, coApplicantCase);

        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberFilterRequest())))
            .thenReturn(List.of(mainApplicantCase));
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberCoApplicantFilterRequest())))
            .thenReturn(List.of(coApplicantCase));

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedPhoneNumberCaseData(), UUID.randomUUID());

        assertThat(previousCase.orElseThrow().getKey()).isEqualTo(coApplicantCase.getKey());
    }

    @ParameterizedTest
    @MethodSource("invalidIdvOrAmlCaseData")
    void findPreviousCase_invalidIdvOrAml_emptyOptionalReturned(Map<String, Object> caseData) {
        final Case sampleCase = new Case()
            .setCaseData(caseData)
            .setKey(UUID.randomUUID())
            .setLastModifiedDate(Instant.now());
        when(caseDataService.filterCasesByCaseDefinitionId(any(), eq(phoneNumberFilterRequest())))
            .thenReturn(List.of(sampleCase));
        setupCaseByKey(sampleCase);

        Optional<Case> previousCase = caseFinder.findPreviousCase(verifiedPhoneNumberCaseData(), UUID.randomUUID());

        assertThat(previousCase).isEmpty();
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> invalidIdvOrAmlCaseData() {
        return Stream.of(
            Arguments.of(idvDeniedCaseData()),
            Arguments.of(idvReviewFailCaseData()),
            Arguments.of(amlFailedCaseData())
        );
    }

    private void setupCaseByKey(Case... cases) {
        Arrays.stream(cases)
            .forEach(c -> when(caseDataService.getCaseByKey(c.getKey()))
                .thenReturn(c));
    }

    private Case validIdvAndAmlCase() {
        return new Case()
            .setCaseData(Map.of(MAIN_APPLICANT_FIELD, Map.of(
                "identityVerificationResult", Map.of(
                    "manualReviewResult", "approved")),
                "antiMoneyLaunderingInfo", Map.of(
                    "status", "SUCCESS"),
                IS_MAIN_APPLICANT_FLOW, true))
            .setKey(UUID.randomUUID())
            .setLastModifiedDate(Instant.now());
    }

    private static Map<String, Object> idvDeniedCaseData() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            "identityVerificationResult", Map.of(
                "documentStatus", "DENIED_FRAUD")),
            IS_MAIN_APPLICANT_FLOW, true);
    }

    private static Map<String, Object> idvReviewFailCaseData() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            "identityVerificationResult", Map.of(
                "manualReviewResult", "fail")),
            IS_MAIN_APPLICANT_FLOW, true);
    }

    private static Map<String, Object> amlFailedCaseData() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            "antiMoneyLaunderingInfo", Map.of(
                "status", "FAILED")),
            IS_MAIN_APPLICANT_FLOW, true);
    }

    private Map<String, Object> verifiedEmailCaseData() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            EMAIL_FIELD, "harry@example.com", EMAIL_VERIFIED_FIELD, true),
            IS_MAIN_APPLICANT_FLOW, true);
    }

    private Map<String, Object> verifiedPhoneNumberCaseData() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            PHONE_NUMBER_FIELD, "+1123123123", PHONE_NUMBER_VERIFIED_FIELD, true),
            IS_MAIN_APPLICANT_FLOW, true);
    }

    private Map<String, Object> verifiedPhoneNumberCaseDataWithCoApplicantId() {
        return Map.of(MAIN_APPLICANT_FIELD, Map.of(
            PHONE_NUMBER_FIELD, "+1123123123", PHONE_NUMBER_VERIFIED_FIELD, true),
            "coApplicantId", "not null co-applicant id",
            IS_MAIN_APPLICANT_FLOW, false);
    }

    private CaseFilterRequest emailFilterRequest() {
        return new CaseFilterRequest()
            .setPropertyName(MAIN_APPLICANT_FIELD + "." + EMAIL_FIELD)
            .setPropertyValues(Collections.singletonList("harry@example.com"))
            .setIncludeArchived(false);
    }

    private CaseFilterRequest phoneNumberFilterRequest() {
        return new CaseFilterRequest()
            .setPropertyName(MAIN_APPLICANT_FIELD + "." + PHONE_NUMBER_FIELD)
            .setPropertyValues(Collections.singletonList("+1123123123"))
            .setIncludeArchived(false);
    }

    private CaseFilterRequest phoneNumberCoApplicantFilterRequest() {
        return new CaseFilterRequest()
            .setPropertyName("coApplicant." + PHONE_NUMBER_FIELD)
            .setPropertyValues(Collections.singletonList("+1123123123"))
            .setIncludeArchived(false);
    }
}
