package com.backbase.flow.onboarding.us.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OnboardingCaseDataUtilsTest {

    private static final String CO_APPLICANT_ID = "coApplicantId";
    private static final String MAIN_APPLICANT = "mainApplicant";
    private static final String CO_APPLICANT = "coApplicant";

    @Test
    void isMainApplicantForMap_hasNoCoApplicantId_returnsTrue() {
        Map<String, Object> caseData = Map.of(CO_APPLICANT, Map.of("firstName", "None of your business!"));

        boolean result = OnboardingCaseDataUtils.isMainApplicantFlow(caseData);

        assertThat(result).isTrue();
    }

    @Test
    void isMainApplicantForMap_hasCoApplicantId_returnsFalse() {
        Map<String, Object> caseData = Map
            .of(CO_APPLICANT_ID, "123-abc", CO_APPLICANT, Map.of("firstName", "None of your business!"));

        boolean result = OnboardingCaseDataUtils.isMainApplicantFlow(caseData);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isMainApplicantForObject(boolean isMainApplicant) {
        final Onboarding onboarding = new Onboarding();
        if (!isMainApplicant) {
            onboarding.setCoApplicantId(UUID.randomUUID().toString());
            onboarding.setIsMainApplicantFlow(false);
        } else {
            onboarding.setIsMainApplicantFlow(true);
        }

        boolean mainApplicantResult = OnboardingCaseDataUtils.isMainApplicantFlow(onboarding);

        assertThat(mainApplicantResult).isEqualTo(isMainApplicant);
    }

    @Test
    void getApplicantForMap_hasNoCoApplicantId_returnsMainApplicant() {
        final Map<String, Object> mainApplicant = Map.of("firstName", "main applicant");
        final Map<String, Object> onboarding = Map.of(MAIN_APPLICANT, mainApplicant);

        final var result = OnboardingCaseDataUtils.getApplicant(onboarding);

        assertThat(result).isEqualTo(mainApplicant);
    }

    @Test
    void getApplicantForMap_hasCoApplicantId_returnsCoApplicant() {
        final Map<String, Object> coApplicant = Map.of("firstName", "co applicant");
        final Map<String, Object> onboarding = Map.of(CO_APPLICANT_ID, "coApplicantId", CO_APPLICANT, coApplicant);

        final var result = OnboardingCaseDataUtils.getApplicant(onboarding);

        assertThat(result).isEqualTo(coApplicant);
    }

    @Test
    void getApplicantForObject_hasNoCoApplicantId_returnsMainApplicant() {
        final Applicant mainApplicant = new Applicant().withFirstName("main applicant");
        final Onboarding onboarding = new Onboarding().withMainApplicant(mainApplicant).withIsMainApplicantFlow(true);

        final Applicant result = OnboardingCaseDataUtils.getApplicant(onboarding);

        assertThat(result).isEqualTo(mainApplicant);
    }

    @Test
    void getApplicantForObject_hasCoApplicantId_returnsCoApplicant() {
        final Applicant coApplicant = new Applicant().withFirstName("co applicant");
        final Onboarding onboarding = new Onboarding().withCoApplicantId("coApplicantId").withCoApplicant(coApplicant);

        final Applicant result = OnboardingCaseDataUtils.getApplicant(onboarding);

        assertThat(result).isEqualTo(coApplicant);
    }

    @Test
    void setApplicantForMap_hasNoCoApplicantId_setsMainApplicant() {
        final Map<String, Object> mainApplicant = Map.of("firstName", "main applicant");
        final Map<String, Object> onboarding = new HashMap<>();

        OnboardingCaseDataUtils.setApplicant(onboarding, mainApplicant);

        assertThat(onboarding.get(MAIN_APPLICANT)).isEqualTo(mainApplicant);
    }

    @Test
    void setApplicantForMap_hasCoApplicantId_setsCoApplicant() {
        final Map<String, Object> coApplicant = Map.of("firstName", "co applicant");
        final Map<String, Object> onboarding = new HashMap<>(Map.of(CO_APPLICANT_ID, "coApplicantId"));

        OnboardingCaseDataUtils.setApplicant(onboarding, coApplicant);

        assertThat(onboarding.get(CO_APPLICANT)).isEqualTo(coApplicant);
    }

    @Test
    void setApplicantForObject_hasNoCoApplicantId_setsMainApplicant() {
        final Applicant mainApplicant = new Applicant().withFirstName("main applicant");
        final Onboarding onboarding = new Onboarding().withIsMainApplicantFlow(true);

        OnboardingCaseDataUtils.setApplicant(onboarding, mainApplicant);

        assertThat(onboarding.getMainApplicant()).isEqualTo(mainApplicant);
    }

    @Test
    void setApplicantForObject_hasCoApplicantId_setsCoApplicant() {
        final Applicant coApplicant = new Applicant().withFirstName("co applicant");
        final Onboarding onboarding = new Onboarding().withCoApplicantId("coApplicantId");

        OnboardingCaseDataUtils.setApplicant(onboarding, coApplicant);

        assertThat(onboarding.getCoApplicant()).isEqualTo(coApplicant);
    }

}
