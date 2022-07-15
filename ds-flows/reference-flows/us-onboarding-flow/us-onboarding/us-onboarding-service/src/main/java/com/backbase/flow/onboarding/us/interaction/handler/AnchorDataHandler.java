package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.error.OnboardingError.ANCHOR_DATA_FAILED;
import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.AnchorDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.utils.CaseDataUtils;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component("submit-anchor-data")
@RequiredArgsConstructor
public class AnchorDataHandler extends ActionHandlerWithValidation<AnchorDataDto, OnboardingDto> {

    private static final String CO_APPLICANT_ANCHOR_DATA_STEP = "co-applicant-data";
    private final CaseDataUtils caseDataUtils;
    private final JourneyMapper<Onboarding> journeyMapper;

    @Override
    public ActionResult<OnboardingDto> handleWithValidation(AnchorDataDto anchorData, InteractionContext context) {
        final UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);
        Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());

        final boolean isCoApplicantDataStep = CO_APPLICANT_ANCHOR_DATA_STEP.equalsIgnoreCase(context.getCurrentStep());

        if (isCoApplicantDataStep || !OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            setCoApplicantData(onboarding, anchorData);
        } else {
            setMainApplicantData(onboarding, anchorData);
        }

        final Case aCase = journeyMapper.write(onboarding, null, null, caseKey.toString());
        onboarding = journeyMapper.read(null, null, aCase);
        final OnboardingDto onboardingDto = MAPPER.mapToDto(onboarding);

        if (!ageVerified(anchorData.getDateOfBirth(), context)) {
            return new ActionResult<>(onboardingDto, ANCHOR_DATA_FAILED
                .getErrorWithContext("dateOfBirth", "Customer's age should be between 18 and 100 years old."));
        }

        return new ActionResult<>(onboardingDto);
    }

    private void setCoApplicantData(Onboarding onboarding, AnchorDataDto anchorData) {
        Applicant coApplicant = ObjectUtils.defaultIfNull(onboarding.getCoApplicant(), new Applicant());

        coApplicant.setFirstName(anchorData.getFirstName());
        coApplicant.setLastName(anchorData.getLastName());
        coApplicant.setDateOfBirth(anchorData.getDateOfBirth());
        coApplicant.setEmail(anchorData.getEmail());

        onboarding.setCoApplicant(coApplicant);
    }

    private void setMainApplicantData(Onboarding onboarding, AnchorDataDto anchorData) {
        Applicant mainApplicant = onboarding.getMainApplicant();

        mainApplicant.setFirstName(anchorData.getFirstName());
        mainApplicant.setLastName(anchorData.getLastName());
        mainApplicant.setDateOfBirth(anchorData.getDateOfBirth());
        mainApplicant.setEmail(anchorData.getEmail());
    }

    private boolean ageVerified(final String dateOfBirth, final InteractionContext context) {
        final int age = Period.between(LocalDate.parse(dateOfBirth), LocalDate.now()).getYears();

        final List<Map<String, Object>> decisionResponse = context.checkDecisionTable("age-verification",
            null, Map.of("age", age));

        return (boolean) decisionResponse.get(0).get("age-accepted");
    }
}
