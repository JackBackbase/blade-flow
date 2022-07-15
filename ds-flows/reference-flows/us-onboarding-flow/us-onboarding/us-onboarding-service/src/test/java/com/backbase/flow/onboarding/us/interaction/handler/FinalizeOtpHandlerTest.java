package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.error.OnboardingError.RESUME_FINALIZE_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.application.uso.casedata.IdentityVerificationResult;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.resume.CoApplicantAwareCaseFinder;
import com.backbase.flow.service.dto.OtpChannel;
import com.backbase.flow.service.dto.OtpDto;
import com.backbase.flow.service.handler.VerifyOtpHandler;
import com.backbase.flow.service.resume.util.ResumeUtils;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FinalizeOtpHandlerTest {

    private final VerifyOtpHandler verifyOtpHandler = mock(VerifyOtpHandler.class);
    private final JourneyMapper<Onboarding> mapper = mock(JourneyMapper.class);
    private final CoApplicantAwareCaseFinder caseFinder = mock(CoApplicantAwareCaseFinder.class);
    private final ResumeUtils resumeUtils = mock(ResumeUtils.class);
    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final FinalizeOtpHandler finalizeOtpHandler = new FinalizeOtpHandler(verifyOtpHandler, mapper, caseFinder,
        resumeUtils);

    @BeforeEach
    void setUp() {
        final UUID caseKey = UUID.randomUUID();
        when(interactionContext.getCaseKey()).thenReturn(caseKey);
    }

    @Test
    void handle_verifyOtpReturnsError_sameErrorIsReturned() {
        var resultWithError = invalidOtpVerificationResult();
        when(verifyOtpHandler.handle(any(OtpDto.class), any()))
            .thenReturn(resultWithError);

        var result = finalizeOtpHandler.handle(verifyOtpDto(), interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo(resultWithError.getErrors().get(0).getKey());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(resultWithError.getErrors().get(0).getMessage());
    }

    @Test
    void handle_notVerifiedOtp_errorIsReturned() {
        when(verifyOtpHandler.handle(any(OtpDto.class), any()))
            .thenReturn(validOtpVerificationResult());
        when(mapper.read(nullable(String.class), nullable(String.class), anyString()))
            .thenReturn(onboardingWithoutOtpVerification());

        var result = finalizeOtpHandler.handle(verifyOtpDto(), interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo(RESUME_FINALIZE_FAILED.getError().getKey());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(RESUME_FINALIZE_FAILED.getError().getMessage());
    }

    @ParameterizedTest
    @MethodSource("onboardingWithInvalidIdvOrAmlOrCitizenship")
    void handle_verifiedOtp_invalidIdvOrAml_errorIsReturned(Onboarding samplePreviousOnboarding) {
        when(verifyOtpHandler.handle(any(OtpDto.class), any()))
            .thenReturn(validOtpVerificationResult());
        when(mapper.read(nullable(String.class), nullable(String.class), anyString()))
            .thenReturn(onboardingWithPhoneOtpVerification());
        when(interactionContext.getCaseData(Map.class))
            .thenReturn(Map.of());

        Case samplePreviousCase = new Case();
        samplePreviousCase.setCaseData(samplePreviousOnboarding);
        //noinspection unchecked
        when(caseFinder.findPreviousCase(any(Map.class), any(UUID.class)))
            .thenReturn(Optional.of(samplePreviousCase));

        var result = finalizeOtpHandler.handle(verifyOtpDto(), interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo(RESUME_FINALIZE_FAILED.getError().getKey());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(RESUME_FINALIZE_FAILED.getError().getMessage());
    }

    @ParameterizedTest
    @MethodSource("onboardingWithValidIdvAndAmlAndCitizenship")
    void handle_verifiedOtp_validIdvOrAml_caseIsSwapped_otpVerificationIsReturned(Onboarding samplePreviousOnboarding) {
        var otpVerification = validOtpVerificationResult();
        when(verifyOtpHandler.handle(any(OtpDto.class), any()))
            .thenReturn(otpVerification);
        when(mapper.read(nullable(String.class), nullable(String.class), anyString()))
            .thenReturn(onboardingWithPhoneOtpVerification());
        when(interactionContext.getCaseData(Map.class))
            .thenReturn(Map.of());
        var interactionId = UUID.randomUUID();
        when(interactionContext.getInteractionId())
            .thenReturn(interactionId);
        var samplePreviousCase = getSamplePreviousCase(samplePreviousOnboarding);
        when(caseFinder.findPreviousCase(any(Map.class), any(UUID.class)))
            .thenReturn(Optional.of(samplePreviousCase));

        var result = finalizeOtpHandler.handle(verifyOtpDto(), interactionContext);

        verify(resumeUtils).swapUnderlyingCase(
            interactionId,
            samplePreviousCase.getKey(),
            samplePreviousCase.getVersion());

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result).isEqualTo(otpVerification);
    }

    private Case getSamplePreviousCase(Onboarding samplePreviousOnboarding) {
        UUID caseKey = UUID.randomUUID();
        long caseVersion = 1232456L;
        return new Case()
            .setKey(caseKey)
            .setVersion(caseVersion)
            .setCaseData(samplePreviousOnboarding);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> onboardingWithInvalidIdvOrAmlOrCitizenship() {
        return Stream.of(
            Arguments.of(onboardingWithIdvDenied()),
            Arguments.of(onboardingWithIdvReviewFail()),
            Arguments.of(onboardingWithAmlFailed()),
            Arguments.of(onboardingWithCitizenshipNotApproved())
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> onboardingWithValidIdvAndAmlAndCitizenship() {
        return Stream.of(
            Arguments.of(onboardingWithAmlSuccessIdvApproved()),
            Arguments.of(onboardingWithAmlSuccessIdvReviewApproved()),
            Arguments.of(onboardingWithAmlSuccessIdvApprovedCitizenshipApproved())
        );
    }

    private OtpDto verifyOtpDto() {
        return OtpDto.builder()
            .phoneNumber("+1234567890")
            .channel(OtpChannel.SMS.name())
            .otp("123456")
            .build();
    }

    private ActionResult<Map<String, Object>> validOtpVerificationResult() {
        return new ActionResult<>(
            Map.of("mainApplicant", Map.of(
                "phoneNumberVerified", true,
                "phoneNumber", "+1234567890")));
    }

    private ActionResult<Map<String, Object>> invalidOtpVerificationResult() {
        return new ActionResult<>(null, Errors.buildError("error", "error message"));
    }

    private Onboarding onboardingWithoutOtpVerification() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withPhoneNumberVerified(false)
                .withEmailVerified(false))
            .withIsMainApplicantFlow(true);
    }

    private Onboarding onboardingWithPhoneOtpVerification() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withPhoneNumberVerified(true)
                .withEmailVerified(false))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithAmlSuccessIdvApproved() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("APPROVED_VERIFIED"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }


    private static Onboarding onboardingWithAmlSuccessIdvReviewApproved() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("IN_REVIEW")
                    .withManualReviewResult("approved"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithAmlSuccessIdvApprovedCitizenshipApproved() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipReview(new CitizenshipReview()
                        .withNeeded(true)
                        .withApproved(true)))
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("IN_REVIEW")
                    .withManualReviewResult("approved"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithIdvDenied() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("DENIED_FRAUD"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithIdvReviewFail() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("IN_REVIEW")
                    .withManualReviewResult("fail"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithCitizenshipNotApproved() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("APPROVED_VERIFIED"))
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipReview(new CitizenshipReview()
                        .withNeeded(true)
                        .withApproved(false)))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
    }

    private static Onboarding onboardingWithAmlFailed() {
        return new Onboarding()
            .withMainApplicant(new Applicant()
                .withIdentityVerificationResult(new IdentityVerificationResult()
                    .withDocumentStatus("APPROVED_VERIFIED"))
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.FAILED)))
            .withIsMainApplicantFlow(true);
    }

}