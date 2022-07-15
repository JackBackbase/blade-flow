package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.service.dto.OtpChannel;
import com.backbase.flow.service.dto.OtpDto;

public class OtpVerificationStepCalls extends BaseIT {

    public static final String MAIN_APPLICANT_PHONE_NUMBER = "+1234567890";
    public static final String CO_APPLICANT_PHONE_NUMBER = "+1234567891";

    public static InteractionResponseDto requestOtp(final String interactionId) throws Exception {
        return requestOtp(interactionId, MAIN_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto requestOtpCoApplicant(final String interactionId) throws Exception {
        return requestOtp(interactionId, CO_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto requestOtp(final String interactionId, String phoneNumber) throws Exception {
        final OtpDto otpRequestDto = OtpDto.builder()
            .phoneNumber(phoneNumber)
            .channel(OtpChannel.SMS.name())
            .build();
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(otpRequestDto);
        return performAction("request-otp", request);
    }

    public static InteractionResponseDto verifyOtpFinalize(final String interactionId) throws Exception {
        return verifyOtpFinalize(interactionId, MAIN_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto verifyOtpFinalizeCoApplicant(final String interactionId) throws Exception {
        return verifyOtpFinalize(interactionId, CO_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto verifyOtpFinalize(final String interactionId, String phoneNumber)
        throws Exception {
        final OtpDto verifyOtpDto = createPayload(phoneNumber, "123456");

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(verifyOtpDto);

        return performAction("verify-otp-finalize", request);
    }

    public static InteractionResponseDto verifyOtp(final String interactionId) throws Exception {
        return verifyOtp(interactionId, MAIN_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto verifyOtpCoApplicant(final String interactionId) throws Exception {
        return verifyOtp(interactionId, CO_APPLICANT_PHONE_NUMBER);
    }

    public static InteractionResponseDto verifyOtp(final String interactionId, String phoneNumber) throws Exception {
        final OtpDto verifyOtpDto = createPayload(phoneNumber, "123456");

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(verifyOtpDto);

        return performAction("verify-otp", request);
    }

    public static InteractionResponseDto verifyWithInvalidCode(final String interactionId) throws Exception {
        final OtpDto verifyOtpDto = createPayload("+1234567890", "000000");

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(verifyOtpDto);

        return performAction("verify-otp", request);
    }

    public static InteractionResponseDto verifyWithInvalidOtp(final String interactionId) throws Exception {
        final OtpDto verifyOtpDto = createPayload("+12345678915", "123456");

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(verifyOtpDto);

        return performAction("verify-otp", request);
    }

    public static InteractionResponseDto verifyFinalizeWithInvalidOtp(final String interactionId) throws Exception {
        final OtpDto verifyOtpDto = createPayload("+12345678915", "123456");

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(verifyOtpDto);

        return performAction("verify-otp-finalize", request);
    }

    public static InteractionResponseDto fetchOtpData(String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        return performAction("fetch-otp-data", request);
    }

    private static OtpDto createPayload(String phoneNumber, String otp) {
        return OtpDto.builder()
            .phoneNumber(phoneNumber)
            .channel(OtpChannel.SMS.name())
            .otp(otp)
            .build();
    }
}
