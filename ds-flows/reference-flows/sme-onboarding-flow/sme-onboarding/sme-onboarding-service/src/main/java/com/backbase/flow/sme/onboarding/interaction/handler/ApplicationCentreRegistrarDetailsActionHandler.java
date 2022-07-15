package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.RegistrarResponseDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import org.springframework.stereotype.Component;

@Component("send-registrar-details")
public class ApplicationCentreRegistrarDetailsActionHandler
    extends ValidationActionHandler<Void, RegistrarResponseDto> {

    @Override
    protected ActionResult<RegistrarResponseDto> handleValidData(
        Void payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);
        if (applicant.isEmpty()) {
            return new ActionResult<>(null, SmeErrors.INPUT_REGISTRAR_INVALID);
        }
        return new ActionResult<>(new RegistrarResponseDto(applicant.get().getFirstName()
            + " " + applicant.get().getLastName()));
    }

}
