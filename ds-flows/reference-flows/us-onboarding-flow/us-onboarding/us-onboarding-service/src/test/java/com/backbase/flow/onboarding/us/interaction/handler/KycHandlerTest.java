package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.AmlResult;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.KycDto;
import com.backbase.flow.onboarding.us.kyc.dto.AnswerDto;
import com.backbase.flow.onboarding.us.kyc.service.KycService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KycHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private final KycService kycService = new KycService(objectMapper);
    private final JourneyMapper<Onboarding> journeyMapper = mock(JourneyMapper.class);

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final KycHandler kycHandler = new KycHandler(kycService, journeyMapper);

    @Test
    void handleKyc() {
        var caseKey = UUID.randomUUID();

        Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withAmlResult(new AmlResult()
                        .withMatchStatus("no_match"))))
            .withIsMainApplicantFlow(true);

        Case aCase = new Case()
            .setKey(caseKey)
            .setCaseData(onboarding);

        when(journeyMapper.read(any(), any(), anyString())).thenReturn(onboarding);
        when(journeyMapper.read(any(), any(), any(Case.class))).thenReturn(onboarding);
        when(journeyMapper.write(any(Onboarding.class), any(), any(), anyString())).thenReturn(aCase);

        when(interactionContext.getCaseKey()).thenReturn(caseKey);

        KycDto kycDto = simpleAnswersDto();

        var result = kycHandler.handle(kycDto, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
    }

    private KycDto simpleAnswersDto() {
        AnswerDto occupation = new AnswerDto();
        occupation.setId("occupation");
        occupation.setSelectedOptions(List.of("Clerical"));

        AnswerDto sourceOfIncome = new AnswerDto();
        sourceOfIncome.setId("sourceOfIncome");
        sourceOfIncome.setSelectedOptions(List.of("Payroll"));

        AnswerDto purpose = new AnswerDto();
        purpose.setId("purpose");
        purpose.setSelectedOptions(List.of("Salary and salary compensation"));

        KycDto kycDto = new KycDto();
        kycDto.setAnswers(List.of(occupation, sourceOfIncome, purpose));

        return kycDto;
    }
}
