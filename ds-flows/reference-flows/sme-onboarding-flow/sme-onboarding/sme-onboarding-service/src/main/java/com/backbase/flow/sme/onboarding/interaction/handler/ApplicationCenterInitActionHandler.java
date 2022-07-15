package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.integration.service.customer.interaction.handler.documentrequest.single.ResponseDto;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.data.repository.InteractionInstanceRepository;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.ApplicationCenterRequestDto;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("application-center-init")
public class ApplicationCenterInitActionHandler
    extends ValidationActionHandler<ApplicationCenterRequestDto, ResponseDto> {

    private final CaseDataService caseDataService;
    private final InteractionInstanceRepository interactionInstanceRepository;

    @Override
    public ActionResult<ResponseDto> handleValidData(
        ApplicationCenterRequestDto payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        if (Objects.isNull(payload)) {
            return new ActionResult<>(null, SmeErrors.INPUT_DATA_INVALID);
        }
        var caseInstance = caseDataService.getCaseByKey(UUID.fromString(payload.getId()));
        bindCaseToInteraction(context, caseInstance);
        return new ActionResult<>(null);
    }

    private void bindCaseToInteraction(InteractionContext interactionContext, Case caseInstance) {
        var interactionId = interactionContext.getInteractionId();
        interactionInstanceRepository.findById(interactionId)
            .map(interactionInstance -> {
                if (interactionInstance.getCaseKey() == null) {
                    interactionInstance.setCaseKey(caseInstance.getKey());
                }
                return interactionInstance;
            }).ifPresent(interactionContext::setInteraction);
    }

}
