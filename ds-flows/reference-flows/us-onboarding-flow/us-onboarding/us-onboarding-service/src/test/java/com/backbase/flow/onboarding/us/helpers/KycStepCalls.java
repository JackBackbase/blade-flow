package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.KycDto;
import com.backbase.flow.onboarding.us.kyc.dto.AnswerDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KycStepCalls extends BaseIT {

    public static InteractionResponseDto submitKycQuestionsUsingJwt(final String interactionId, boolean amlNoMatch)
        throws Exception {
        return performActionWithJwt("submit-kyc-data", createPayload(interactionId, amlNoMatch));
    }

    public static InteractionResponseDto submitKycQuestions(final String interactionId, boolean amlNoMatch)
        throws Exception {
        return performAction("submit-kyc-data", createPayload(interactionId, amlNoMatch));
    }

    public static InteractionResponseDto requestKycQuestionsUsingJwt(final String interactionId)
        throws Exception {
        return performActionWithJwt("kyc-questions-loader", createKycQuestionsPayload(interactionId));
    }

    public static InteractionResponseDto requestKycQuestions(final String interactionId)
        throws Exception {
        return performAction("kyc-questions-loader", createKycQuestionsPayload(interactionId));
    }

    private static InteractionRequestDto createKycQuestionsPayload(String interactionId) {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        return request;
    }

    private static InteractionRequestDto createPayload(String interactionId, boolean amlNoMatch) {
        List<AnswerDto> noMatchKycResponse = Arrays.asList(
            createSingleAnswerDto("occupation", "Clerical"),
            createSingleAnswerDto("sourceOfIncome", "Payroll"),
            createSingleAnswerDto("purpose", "Salary and salary compensation")
        );
        List<AnswerDto> matchKycResponse = Arrays.asList(
            createSingleAnswerDto("occupation", "Consultant"),
            createSingleAnswerDto("sourceOfIncome", "Loan Proceeds"),
            createSingleAnswerDto("purpose", "Pension"),
            createSingleAnswerDto("transferTiming", "1-5"),
            createSingleAnswerDto("totalAmount", "1-1000"),
            createSingleAnswerDto("internationalTransfer", "true"),
            createSingleAnswerDto("internationalTransferTiming", "6-10"),
            createSingleAnswerDto("internationalTotalAmount", "1000-2500")
        );

        final KycDto answers = KycDto.builder()
            .answers(amlNoMatch ? noMatchKycResponse : matchKycResponse)
            .build();

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(answers);
        return request;
    }

    private static AnswerDto createSingleAnswerDto(String id, String selectedOption) {
        final AnswerDto answer = new AnswerDto();
        answer.setId(id);
        answer.setSelectedOptions(Collections.singletonList(selectedOption));
        return answer;
    }
}
