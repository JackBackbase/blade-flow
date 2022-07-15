package com.backbase.flow.onboarding.us.kyc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.KycAnswers;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.onboarding.us.interaction.dto.KycDto;
import com.backbase.flow.onboarding.us.kyc.dto.AnswerDto;
import com.backbase.flow.onboarding.us.kyc.dto.formly.KycObject;
import com.backbase.flow.onboarding.us.kyc.dto.formly.Option;
import com.backbase.flow.onboarding.us.kyc.dto.formly.TemplateOptions;
import com.backbase.flow.onboarding.us.kyc.dto.formly.Validators;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    private static final String ID_STRING = "id";
    private static final String ANSWER_STRING_1 = "answer1";
    private static final String ANSWER_STRING_2 = "answer2";
    private static final String KYC_FILENAME = "kyc/kyc.json";

    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private KycService kycService;

    @Test
    void handleKycPageSingleAnswerSuccessfulTest() throws IOException {
        KycObject kycObject = createKycObject(
            ID_STRING,
            Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.emptyList()
        );

        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(new KycObject[]{kycObject});
        KycDto kycDto = createKycPage(ID_STRING, Collections.singletonList(ANSWER_STRING_1));

        ActionResult<KycAnswers> actionResult = kycService.handleKycPage(kycDto, KYC_FILENAME);
        assertThat(actionResult.getErrors()).isEmpty();
    }

    @Test
    void handleKycPageNoMatchingAnswersTest() throws IOException {
        KycObject kycObject = createKycObject(
            ID_STRING,
            Collections.singletonList(ANSWER_STRING_2),
            Collections.emptyList()
        );

        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(new KycObject[]{kycObject});
        KycDto kycDto = createKycPage(ID_STRING, Collections.singletonList(ANSWER_STRING_1));

        ActionResult<KycAnswers> actionResult = kycService.handleKycPage(kycDto, KYC_FILENAME);
        assertThat(actionResult.getErrors()).isNotEmpty();
    }

    @Test
    void handleKycPageSingleAnswerValidationTest() throws IOException {
        KycObject kycObject = createKycObject(
            ID_STRING,
            Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.singletonList("singleOption")
        );

        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(new KycObject[]{kycObject});
        KycDto kycDto = createKycPage(ID_STRING, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2));

        ActionResult<KycAnswers> actionResult = kycService.handleKycPage(kycDto, KYC_FILENAME);
        assertThat(actionResult.getErrors()).isNotEmpty();
    }

    @Test
    void handleKycPageMultipleAnswerSuccessTest() throws IOException {
        KycObject kycObject = createKycObject(ID_STRING, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.emptyList());
        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(new KycObject[]{kycObject});
        KycDto kycDto = createKycPage(ID_STRING, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2));

        ActionResult<KycAnswers> actionResult = kycService.handleKycPage(kycDto, KYC_FILENAME);
        assertThat(actionResult.getErrors()).isEmpty();
    }

    @Test
    void handleKycPageMatchingQuestionsAndAnswersFailTest() throws IOException {
        KycObject kycObject = createKycObject(ID_STRING, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.emptyList());
        KycObject kycObject1 = createKycObject(ID_STRING + 1, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.emptyList());
        KycObject kycObject2 = createKycObject(ID_STRING + 2, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2),
            Collections.emptyList());

        when(objectMapper.readValue(any(String.class), any(Class.class)))
            .thenReturn(new KycObject[]{kycObject, kycObject1, kycObject2});
        KycDto kycDto = createKycPage(ID_STRING, Arrays.asList(ANSWER_STRING_1, ANSWER_STRING_2));

        ActionResult<KycAnswers> actionResult = kycService.handleKycPage(kycDto, KYC_FILENAME);
        assertThat(actionResult.getErrors()).isNotEmpty();
    }

    private KycObject createKycObject(String id, List<String> answers, List<String> validatorList) {
        KycObject kycObject = new KycObject();
        kycObject.setKey(id);

        TemplateOptions templateOptions = new TemplateOptions();

        List<Option> options = new ArrayList<>();
        for (String answer : answers) {
            Option option = new Option();
            option.setLabel(answer);
            option.setValue(answer);
            options.add(option);
        }
        templateOptions.setOptions(options);
        kycObject.setTemplateOptions(templateOptions);
        Validators validators = new Validators();
        validators.setValidation(validatorList);

        kycObject.setTemplateOptions(templateOptions);
        kycObject.setValidators(validators);
        return kycObject;
    }

    private KycDto createKycPage(String id, List<String> answers) {
        KycDto kycDto = new KycDto();
        AnswerDto localKycAnswers = new AnswerDto();
        localKycAnswers.setId(id);
        localKycAnswers.setSelectedOptions(answers);
        List<AnswerDto> answersDtoList = Collections.singletonList(localKycAnswers);
        kycDto.setAnswers(answersDtoList);
        return kycDto;
    }
}
