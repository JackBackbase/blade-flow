package com.backbase.flow.onboarding.us.service.policyagreement;

import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PolicyAgreementService {

    private final JourneyMapper<Onboarding> journeyMapper;

    public Onboarding processPolicies(final PoliciesAgreementDto policiesAgreement,  final UUID caseKey) {

        final Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        List<AgreementInfo> agreements = applicant.getAgreements();
        for (PolicyType policyType : PolicyType.values()) {
            agreeToPolicy(policyType, policiesAgreement.isAgreedToTerms(), agreements);
        }

        sendDataToCbs(caseKey);

        journeyMapper.write(onboarding, null, null, caseKey.toString());

        return onboarding;
    }

    private void agreeToPolicy(PolicyType policyType, boolean hasAgreed, List<AgreementInfo> agreements) {
        AgreementInfo agreement = getExistingOrCreateNewAgreement(policyType, agreements);

        agreement.getAcceptedAt().add(OffsetDateTime.now());
        agreement.setAccepted(hasAgreed);
    }

    private AgreementInfo getExistingOrCreateNewAgreement(PolicyType policyType, List<AgreementInfo> agreements) {
        Optional<AgreementInfo> any = agreements.stream()
            .filter(agreement -> policyType.equals(agreement.getPolicyType())).findAny();
        if (any.isPresent()) {
            return any.get();
        }
        AgreementInfo agreement = new AgreementInfo()
            .withPolicyType(policyType);
        agreements.add(agreement);
        return agreement;
    }

    // TODO: integration with CBS
    private void sendDataToCbs(final UUID caseKey) {
        log.info("Sending case data to Core Banking System for {}", caseKey);
    }

}
