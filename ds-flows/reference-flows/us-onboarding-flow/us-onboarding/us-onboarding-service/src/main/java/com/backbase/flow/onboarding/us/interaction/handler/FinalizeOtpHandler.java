package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.error.OnboardingError.RESUME_FINALIZE_FAILED;
import static java.util.Objects.nonNull;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.resume.CoApplicantAwareCaseFinder;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.dto.OtpDto;
import com.backbase.flow.service.handler.VerifyOtpHandler;
import com.backbase.flow.service.resume.util.ResumeUtils;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("verify-otp-finalize")
@RequiredArgsConstructor
@Slf4j
public class FinalizeOtpHandler implements ActionHandler<OtpDto, Map<String, Object>> {

    private final VerifyOtpHandler verifyOtpHandler;
    private final JourneyMapper<Onboarding> mapper;
    private final CoApplicantAwareCaseFinder caseFinder;
    private final ResumeUtils resumeUtils;

    @Override
    public ActionResult<Map<String, Object>> handle(OtpDto verifyOtpDto, InteractionContext context) {

        ActionResult<Map<String, Object>> otpVerification = verifyOtpHandler.handle(verifyOtpDto, context);

        if (!otpVerification.isSuccessful()) {
            Errors errors = new Errors();
            otpVerification.getErrors().forEach(errors::add);
            return new ActionResult<>(null, errors);
        }

        final Onboarding onboarding = mapper.read(null, null, context.getCaseKey().toString());
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        if (!hasVerifiedOtp(applicant)) {
            return new ActionResult<>(null, RESUME_FINALIZE_FAILED.getError());
        }

        if (!loadPreviousCase(context)) {
            return new ActionResult<>(null, RESUME_FINALIZE_FAILED.getError());
        }

        return otpVerification;
    }

    private boolean hasVerifiedOtp(Applicant applicant) {
        return ((nonNull(applicant.getPhoneNumberVerified()) && applicant.getPhoneNumberVerified())
            || (nonNull(applicant.getEmailVerified()) && applicant.getEmailVerified()));
    }

    private boolean loadPreviousCase(InteractionContext context) {
        //noinspection unchecked
        final Map<String, Object> currentCaseData = context.getCaseData(Map.class);
        final Optional<Case> optionalCase = caseFinder.findPreviousCase(currentCaseData, context.getCaseKey());

        if (optionalCase.isPresent()) {
            final Case aCase = optionalCase.get();
            final Applicant applicant = OnboardingCaseDataUtils.getApplicant(aCase.getCaseData(Onboarding.class));

            if (isAmlIdvCitizenshipValidToFinalize(applicant)) {
                resumeUtils.swapUnderlyingCase(context.getInteractionId(), aCase.getKey(), aCase.getVersion());
                return true;
            }
        }
        return false;
    }

    private boolean isAmlIdvCitizenshipValidToFinalize(Applicant applicant) {
        return (isIdvApproved(applicant) || isIdvReviewApproved(applicant))
            && isAmlSuccess(applicant)
            && (isCitizenshipReviewNotNeeded(applicant) || isCitizenshipDataApproved(applicant));
    }

    private boolean isCitizenshipReviewNotNeeded(Applicant applicant) {
        return applicant.getCitizenship() == null
            || applicant.getCitizenship().getCitizenshipReview() == null
            || Boolean.FALSE.equals(applicant.getCitizenship().getCitizenshipReview().getNeeded());
    }

    private boolean isCitizenshipDataApproved(Applicant applicant) {
        return Boolean.TRUE.equals(applicant.getCitizenship().getCitizenshipReview().getApproved());
    }

    private boolean isIdvApproved(Applicant applicant) {
        return applicant.getIdentityVerificationResult() != null
            && "APPROVED_VERIFIED".equals(applicant.getIdentityVerificationResult().getDocumentStatus());
    }

    private boolean isIdvReviewApproved(Applicant applicant) {
        return applicant.getIdentityVerificationResult() != null
            && "approved".equals(applicant.getIdentityVerificationResult().getManualReviewResult());
    }

    private boolean isAmlSuccess(Applicant applicant) {
        return applicant.getAntiMoneyLaunderingInfo() != null
            && Status.SUCCESS.equals(applicant.getAntiMoneyLaunderingInfo().getStatus());
    }

}
