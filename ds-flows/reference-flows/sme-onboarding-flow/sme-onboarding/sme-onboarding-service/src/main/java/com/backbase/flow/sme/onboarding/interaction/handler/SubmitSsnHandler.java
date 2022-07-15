package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.SsnDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component("submit-ssn")
public class SubmitSsnHandler extends ValidationActionHandler<SsnDto, Void> {

    @Override
    protected ActionResult<Void> handleValidData(
        SsnDto payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        if (Objects.isNull(payload)) {
            return new ActionResult<>(null, SmeErrors.INPUT_DATA_INVALID);
        }

        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);
        if (applicant.isEmpty()) {
            return new ActionResult<>(null, SmeErrors.INPUT_REGISTRAR_INVALID);
        }
        applicant.get().setSsn(payload.getSsn());
        context.saveCaseDataToReadCaseData(smeCase);

        return new ActionResult<>(null);
    }


}
