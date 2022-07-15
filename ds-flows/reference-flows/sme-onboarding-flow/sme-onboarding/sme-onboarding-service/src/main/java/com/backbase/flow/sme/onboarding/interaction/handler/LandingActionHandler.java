package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.events.InteractionEngineEventPublishingService;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.event.ApplicationSubmittedEvent;
import com.backbase.flow.sme.onboarding.interaction.model.LandingResponseDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("sme-onboarding-landing-data")
public class LandingActionHandler extends ValidationActionHandler<Void, LandingResponseDto> {

    private final InteractionEngineEventPublishingService publishingService;

    @Override
    protected ActionResult<LandingResponseDto> handleValidData(
        Void payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        var event = new ApplicationSubmittedEvent(context.getCaseKey());

        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);
        if (applicant.isEmpty()) {
            return new ActionResult<>(null, SmeErrors.INPUT_REGISTRAR_INVALID);
        }

        publishingService.publish(context, event.getDescription(), event);
        //setting this variable to allow triggering of the conditional event "dataGatheringComplete".
        smeCase.setIsLanded(true);

        context.saveCaseDataToReadCaseData(smeCase);

        return new ActionResult<>(
            new LandingResponseDto(
                context.getCaseKey(),
                applicant.get().getEmail()
            )
        );
    }
}
