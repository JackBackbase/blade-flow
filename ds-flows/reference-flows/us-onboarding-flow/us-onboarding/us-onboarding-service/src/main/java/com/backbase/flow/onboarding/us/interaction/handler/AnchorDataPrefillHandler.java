package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("prefill-anchor-data")
@RequiredArgsConstructor
public class AnchorDataPrefillHandler implements
    ActionHandler<Void, OnboardingDto> {

    private final JourneyMapper<Onboarding> journeyMapper;
    private final CaseDataUtils caseDataUtils;

    @Override
    public ActionResult<OnboardingDto> handle(Void unused, InteractionContext context) {
        UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);
        Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());

        return new ActionResult<>(MAPPER.mapToDto(onboarding));
    }
}
