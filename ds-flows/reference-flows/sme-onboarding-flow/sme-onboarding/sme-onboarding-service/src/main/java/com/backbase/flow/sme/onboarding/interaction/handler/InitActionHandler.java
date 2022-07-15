package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.events.InteractionEngineEventPublishingService;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.TermsConditionsRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.TermsConditionsResponseDto;
import com.backbase.flow.sme.onboarding.mapper.DataMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("sme-onboarding-init")
public class InitActionHandler extends ValidationActionHandler<TermsConditionsRequestDto, TermsConditionsResponseDto> {

    private final DataMapper dataMapper;
    private final InteractionEngineEventPublishingService publishingService;

    @Override
    protected ActionResult<TermsConditionsResponseDto> handleValidData(
        TermsConditionsRequestDto payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        if (Objects.isNull(payload)) {
            return new ActionResult<>(null, SmeErrors.INPUT_DATA_INVALID);
        }

        smeCase.setTermsAndConditions(dataMapper.toTermsAndConditions(payload));
        context.savePreliminaryCaseData(smeCase);

        var event = dataMapper.toTermsAndConditionsEvent(context.getCaseKey());

        publishingService.publish(context, event.getDescription(), event);

        return new ActionResult<>(new TermsConditionsResponseDto(smeCase.getTermsAndConditions().getAccepted(),
            smeCase.getTermsAndConditions().getAcceptanceDate(),
            !smeCase.getApplicants().isEmpty(),
            context.getCaseKey().toString()));
    }

}
