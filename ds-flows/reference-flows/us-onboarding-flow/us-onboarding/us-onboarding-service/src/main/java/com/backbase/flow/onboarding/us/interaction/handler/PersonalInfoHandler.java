package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.error.OnboardingError.ANCHOR_DATA_FAILED;
import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.PersonalInfoDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.handler.ValidationHandler;
import com.backbase.flow.utils.CaseDataUtils;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Validator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component("submit-personal-info")
public class PersonalInfoHandler extends ValidationHandler<PersonalInfoDto, OnboardingDto> {

    private final CaseDataUtils caseDataUtils;
    private final JourneyMapper<Onboarding> journeyMapper;

    public PersonalInfoHandler(Validator validator, CaseDataUtils caseDataUtils, JourneyMapper<Onboarding> mapper) {
        super(validator);
        this.journeyMapper = mapper;
        this.caseDataUtils = caseDataUtils;
    }

    @Override
    public ActionResult<OnboardingDto> handleWithValidation(PersonalInfoDto personalInfo, InteractionContext context) {
        final UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);
        Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());

        if (!OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            setCoApplicantData(onboarding, personalInfo);
        } else {
            setMainApplicantData(onboarding, personalInfo);
        }

        final Case aCase = journeyMapper.write(onboarding, null, null, caseKey.toString());
        onboarding = journeyMapper.read(null, null, aCase);
        final OnboardingDto onboardingDto = MAPPER.mapToDto(onboarding);

        if (!ageVerified(personalInfo.getDateOfBirth(), context)) {
            return new ActionResult<>(onboardingDto, ANCHOR_DATA_FAILED
                .getErrorWithContext("dateOfBirth", "Customer's age should be between 18 and 100 years old."));
        }

        return new ActionResult<>(onboardingDto);
    }

    private void setCoApplicantData(Onboarding onboarding, PersonalInfoDto personalInfo) {
        Applicant coApplicant = ObjectUtils.defaultIfNull(onboarding.getCoApplicant(), new Applicant());

        coApplicant.setFirstName(personalInfo.getFirstName());
        coApplicant.setLastName(personalInfo.getLastName());
        coApplicant.setDateOfBirth(personalInfo.getDateOfBirth());
        coApplicant.setEmail(personalInfo.getEmail());
        coApplicant.setPhoneNumber(personalInfo.getPhoneNumber());

        onboarding.setCoApplicant(coApplicant);
    }

    private void setMainApplicantData(Onboarding onboarding, PersonalInfoDto personalInfo) {
        Applicant mainApplicant = onboarding.getMainApplicant();

        mainApplicant.setFirstName(personalInfo.getFirstName());
        mainApplicant.setLastName(personalInfo.getLastName());
        mainApplicant.setDateOfBirth(personalInfo.getDateOfBirth());
        mainApplicant.setEmail(personalInfo.getEmail());
        mainApplicant.setPhoneNumber(personalInfo.getPhoneNumber());
    }

    private boolean ageVerified(final String dateOfBirth, final InteractionContext context) {
        final int age = Period.between(LocalDate.parse(dateOfBirth), LocalDate.now()).getYears();

        final List<Map<String, Object>> decisionResponse = context.checkDecisionTable("age-verification",
            null, Map.of("age", age));

        return (boolean) decisionResponse.get(0).get("age-accepted");
    }
}
