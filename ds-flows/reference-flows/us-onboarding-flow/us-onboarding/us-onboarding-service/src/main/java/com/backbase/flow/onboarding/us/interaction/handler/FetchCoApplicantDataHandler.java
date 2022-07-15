package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.coapplicant.CoApplicantService;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCoApplicantDataResponseDto;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("fetch-co-applicant-data")
@RequiredArgsConstructor
@Slf4j
public class FetchCoApplicantDataHandler implements
    ActionHandler<CoApplicantDataRequestDto, FetchCoApplicantDataResponseDto> {

    private final CoApplicantService coApplicantService;

    @Override
    public ActionResult<FetchCoApplicantDataResponseDto> handle(CoApplicantDataRequestDto fetchCoJoinerDataDto,
        InteractionContext context) {
        final Optional<Case> optionalCase = coApplicantService
            .getExistingCaseByCoApplicantId(fetchCoJoinerDataDto.getCoApplicantId());

        if (optionalCase.isEmpty()) {
            return new ActionResult<>(null, new Error(
                "Error",
                "No co-applicant data found",
                Map.of()));
        }
        final Onboarding onboarding = optionalCase.get().getCaseData(Onboarding.class);

        final FetchCoApplicantDataResponseDto fetchCustomerDataResponseDto = MAPPER
            .mapToFetchCoApplicantDataResponseDto(onboarding);

        return new ActionResult<>(fetchCustomerDataResponseDto);
    }
}
