package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdtDataCollectionService {

    private final JourneyMapper<Onboarding> onboardingMapper;

    public boolean addressIdtDataCollected(UUID caseKey) {
        final Onboarding caseData = getOnboarding(caseKey);
        return OnboardingCaseDataUtils.getApplicant(caseData).getAddress() != null;
    }

    public boolean idvIdtDataCollected(UUID caseKey) {
        final Onboarding caseData = getOnboarding(caseKey);
        return OnboardingCaseDataUtils
            .getApplicant(caseData)
            .getIdentityVerificationResult()
            .getVerificationId() != null;
    }

    public boolean kycIdtDataCollected(UUID caseKey) {
        final Onboarding caseData = getOnboarding(caseKey);
        return OnboardingCaseDataUtils
            .getApplicant(caseData)
            .getKycInformation() != null;
    }

    public boolean citizenshipDataCollected(UUID caseKey) {
        final Onboarding caseData = getOnboarding(caseKey);
        return OnboardingCaseDataUtils
            .getApplicant(caseData)
            .getCitizenship() != null;
    }

    private Onboarding getOnboarding(UUID caseKey) {
        return onboardingMapper.read(null, null, caseKey.toString());
    }
}
