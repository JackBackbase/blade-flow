package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCitizenshipDataResponseDto;
import com.backbase.flow.utils.CaseDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("fetch-citizenship-data")
@RequiredArgsConstructor
public class FetchCitizenshipDataHandler implements ActionHandler<Void, FetchCitizenshipDataResponseDto> {

    private final JourneyMapper<Applicant> journeyMapper;
    private final CaseDataUtils caseDataUtils;

    /**
     * <p>Reads the Citizenship data context set previously on the case for the purpose of
     * being re-filled during the US Onboarding Flow.<p>
     *
     * @param payload            empty payload sent from the frontend.
     * @param interactionContext the current interaction context.
     * @return an {@link ActionResult} with all Citizenship data in the body or an empty body if data is not found.
     */
    @Override
    public ActionResult<FetchCitizenshipDataResponseDto> handle(Void payload, InteractionContext interactionContext) {
        final String caseKey = caseDataUtils.getOrCreateCaseKey(interactionContext).toString();
        final Applicant applicant = journeyMapper.read(null, null, caseKey);
        return new ActionResult<>(MAPPER.mapToCitizenshipDto(applicant));
    }
}
