package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.OtpEmailDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import org.springframework.stereotype.Component;

@Component("fetch-otp-email")
public class FetchOtpEmailActionHandler extends ValidationActionHandler<Void, OtpEmailDto> {

    @Override
    protected ActionResult<OtpEmailDto> handleValidData(
        Void payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);
        if (applicant.isEmpty()) {
            return new ActionResult<>(null, SmeErrors.INPUT_REGISTRAR_INVALID);
        }
        return new ActionResult<>(new OtpEmailDto(applicant.get().getEmail()));
    }
}
