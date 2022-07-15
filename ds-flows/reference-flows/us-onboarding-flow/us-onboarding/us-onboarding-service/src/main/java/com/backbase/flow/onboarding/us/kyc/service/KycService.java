package com.backbase.flow.onboarding.us.kyc.service;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.application.uso.casedata.Answer;
import com.backbase.flow.application.uso.casedata.KycAnswers;
import com.backbase.flow.onboarding.us.interaction.dto.KycDto;
import com.backbase.flow.onboarding.us.kyc.dto.AnswerDto;
import com.backbase.flow.onboarding.us.kyc.dto.QuestionDto;
import com.backbase.flow.onboarding.us.kyc.dto.QuestionType;
import com.backbase.flow.onboarding.us.kyc.dto.formly.KycObject;
import com.backbase.flow.onboarding.us.kyc.dto.formly.Option;
import com.backbase.flow.onboarding.us.kyc.exception.OnboardingKycError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KycService {

    private final ObjectMapper objectMapper;

    public ActionResult<KycAnswers> handleKycPage(KycDto kycPage,
        String questionsFileName) {
        log.debug("Handling kycPage: {}", kycPage);
        List<AnswerDto> responses = kycPage.getAnswers();
        Map<String, QuestionDto> questions = getQuestions(questionsFileName);
        log.debug("KycQuestions: {}, kycAnswers: {}", questions, responses);
        final Errors kycViolations = validateKycAnswers(questions, responses);

        if (!kycViolations.isEmpty()) {
            return new ActionResult<>(null, kycViolations);
        }

        List<List<Answer>> kycInformationGroups = new ArrayList<>();
        kycInformationGroups.add(responses.stream()
            .map(answerDto -> fromKycAnswersDto(answerDto, questions))
            .collect(Collectors.toList())
        );
        KycAnswers kycAnswers = new KycAnswers();
        kycAnswers.setAnswers(kycInformationGroups);
        return new ActionResult<>(kycAnswers, kycViolations);
    }

    private Map<String, QuestionDto> getQuestions(String filename) {
        try {
            List<KycObject> kycObjects = Arrays.asList(objectMapper.readValue(getFileFromResources(filename),
                KycObject[].class));
            log.debug("Mapping kycObjects to kycQuestionDTOs");

            return kycObjects.stream()
                .map(this::fromKycObject)
                .collect(Collectors.toMap(QuestionDto::getId, Function.identity()));
        } catch (Exception e) {
            log.error("Can't get questions from file: {}", filename);
            return Map.of();
        }
    }

    private QuestionDto fromKycObject(KycObject kycObject) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(kycObject.getKey());
        questionDto.setLabel(kycObject.getTemplateOptions().getLabel());
        questionDto.setType(getKycTypeFromObject(kycObject));
        questionDto.setOptions(getKycAnswersFromObject(kycObject));
        return questionDto;
    }

    private static Errors validateKycAnswers(Map<String, QuestionDto> questions, List<AnswerDto> answers) {
        Errors errors = new Errors();

        log.debug("Checking if all questions been answered");
        Set<String> answersIds = new HashSet<>();
        boolean willTransferInternationally = true;
        for (AnswerDto dto : answers) {
            String id = dto.getId();
            answersIds.add(id);
            if (id.equalsIgnoreCase("internationalTransfer")) {
                willTransferInternationally = Boolean.parseBoolean(dto.getSelectedOptions().get(0));
            }
        }
        if (!willTransferInternationally) {
            questions.remove("internationalTransferTiming");
            questions.remove("internationalTotalAmount");
        }
        if (!answersIds.containsAll(questions.keySet())) {
            log.debug(OnboardingKycError.OUS_001.getMessage());
            errors.add(OnboardingKycError.OUS_001);
            return errors;
        }

        log.debug("Checking if answers match questions");
        for (AnswerDto answer : answers) {
            QuestionDto question = Optional.ofNullable(questions.get(answer.getId()))
                .orElseThrow(RuntimeException::new);

            if (question.getType() == QuestionType.SINGLE && answer.getSelectedOptions().size() > 1) {
                log.debug("Single answer question must have one answer only. Actual number: {}",
                    answer.getSelectedOptions().size());
                errors.add(OnboardingKycError.OUS_002);
                return errors;
            }

            if (!question.getOptions().containsAll(answer.getSelectedOptions())) {
                log.debug("Answers don't match the question");
                errors.add(OnboardingKycError.OUS_003);
                return errors;
            }
        }
        return errors;
    }

    private static Answer fromKycAnswersDto(AnswerDto answerDto, Map<String, QuestionDto> questions) {
        log.debug("Mapping kycAnswersDto to kycInformation. kycAnswersDto: {}", answerDto);
        Answer answer = new Answer();
        answer.setId(answerDto.getId());
        answer.setLabel(questions.get(answerDto.getId()).getLabel());
        answer.setAnswers(answerDto.getSelectedOptions());
        return answer;
    }

    private static List<String> getKycAnswersFromObject(KycObject kycObject) {
        return kycObject.getTemplateOptions().getOptions().stream()
            .map(Option::getValue)
            .collect(Collectors.toList());
    }

    private static QuestionType getKycTypeFromObject(KycObject kycObject) {
        if (kycObject.getValidators().getValidation().contains("singleOption")) {
            return QuestionType.SINGLE;
        } else {
            return QuestionType.MULTIPLE;
        }
    }

    private String getFileFromResources(String fileName) {
        log.debug("Loading the file: {} from resources", fileName);
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(new ClassPathResource(fileName).getInputStream(), writer, StandardCharsets.UTF_8);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("file not found!");
        }
    }
}
