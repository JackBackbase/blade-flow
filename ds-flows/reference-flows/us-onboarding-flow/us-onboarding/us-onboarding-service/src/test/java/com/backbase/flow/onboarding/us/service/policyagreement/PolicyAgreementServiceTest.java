package com.backbase.flow.onboarding.us.service.policyagreement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingReader;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingWriter;
import com.google.common.collect.Lists;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicyAgreementServiceTest {

    private final MessageCorrelationBuilder messageCorrelationBuilder = mock(MessageCorrelationBuilder.class);
    private final CaseDataService caseDataService = mock(CaseDataService.class);

    private final JourneyReader<Onboarding> journeyReader = new OnboardingReader();
    private final JourneyWriter<Onboarding> journeyWriter = new OnboardingWriter(caseDataService);
    private final JourneyMapper<Onboarding> journeyMapper = spy(new JourneyMapper<>(Onboarding.class,
        List.of(journeyReader), List.of(journeyWriter), caseDataService));

    private final PolicyAgreementService policyAgreementService = new PolicyAgreementService(journeyMapper);

    @BeforeEach
    private void setup() {
        when(caseDataService.updateCase(any(Case.class)))
            .thenAnswer(a -> a.getArgument(0));
    }

    @Test
    void processPoliciesWorks() {
        final UUID caseKey = UUID.randomUUID();
        final PoliciesAgreementDto policiesAgreementDto = new PoliciesAgreementDto(true);
        final Onboarding caseData = new Onboarding().withMainApplicant(new Applicant()).withIsMainApplicantFlow(true);
        final Case sampleCase = new Case()
            .setKey(caseKey)
            .setCaseData(caseData);

        when(messageCorrelationBuilder.processInstanceVariableEquals(any(), any()))
            .thenReturn(messageCorrelationBuilder);
        when(caseDataService.getCaseByKey(caseKey)).thenReturn(sampleCase);

        final Onboarding onboarding = policyAgreementService
            .processPolicies(policiesAgreementDto, caseKey);

        verify(journeyMapper).write(any(Onboarding.class), isNull(), isNull(), anyString());

        Condition<AgreementInfo> signedPrivacyPolicy = new Condition<>(
            getSignedAgreementPredicate(PolicyType.PRIVACY_POLICY),
            "Signed Privacy Policy");
        Condition<AgreementInfo> signedTermsAndConditions = new Condition<>(
            getSignedAgreementPredicate(PolicyType.TERMS_AND_CONDITIONS),
            "Signed Terms and Conditions");

        assertThat(onboarding.getMainApplicant().getAgreements().size()).isEqualTo(PolicyType.values().length);
        assertThat(onboarding.getMainApplicant().getAgreements()).haveExactly(1, signedPrivacyPolicy);
        assertThat(onboarding.getMainApplicant().getAgreements()).haveExactly(1, signedTermsAndConditions);
    }

    private Predicate<AgreementInfo> getSignedAgreementPredicate(PolicyType policyType) {
        return agreementInfo -> policyType.equals(agreementInfo.getPolicyType())
            && agreementInfo.getAccepted()
            && agreementInfo.getAcceptedAt().size() == 1;
    }

    @Test
    void processPoliciesPoliciesAcceptedMultipleTimes() {
        final var caseKey = UUID.randomUUID();
        final PoliciesAgreementDto policiesAgreementDto = new PoliciesAgreementDto(true);
        final AgreementInfo privacyAgreementInfo = new AgreementInfo()
            .withAccepted(true)
            .withPolicyType(PolicyType.PRIVACY_POLICY)
            .withAcceptedAt(Lists.newArrayList(OffsetDateTime.now().minusHours(2)));
        final AgreementInfo tecAgreementInfo = new AgreementInfo()
            .withAccepted(true)
            .withPolicyType(PolicyType.TERMS_AND_CONDITIONS)
            .withAcceptedAt(Lists.newArrayList(OffsetDateTime.now().minusHours(2)));

        final Onboarding caseData = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAgreements(Lists.newArrayList(privacyAgreementInfo, tecAgreementInfo)))
            .withIsMainApplicantFlow(true);
        final Case sampleCase = new Case()
            .setKey(caseKey)
            .setCaseData(caseData);

        when(caseDataService.getCaseByKey(caseKey)).thenReturn(sampleCase);

        final Onboarding onboarding = policyAgreementService
            .processPolicies(policiesAgreementDto, caseKey);

        verify(journeyMapper).write(any(Onboarding.class), isNull(), isNull(), anyString());

        assertThat(onboarding.getMainApplicant().getAgreements().get(0).getAcceptedAt())
            .hasSize(2);
        assertThat(onboarding.getMainApplicant().getAgreements().get(1).getAcceptedAt())
            .hasSize(2);
    }
}