package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.error.SmeErrors;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.CaseDataDto;
import com.backbase.flow.sme.onboarding.mapper.DataMapper;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("sme-onboarding-anchor-data")
public class AnchorActionHandler extends ValidationActionHandler<AnchorRequestDto, CaseDataDto> {

    private final DataMapper dataMapper;

    @Override
    protected ActionResult<CaseDataDto> handleValidData(
        AnchorRequestDto payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        if (Objects.isNull(payload)) {
            return new ActionResult<>(null, SmeErrors.INPUT_DATA_INVALID);
        }

        var applicant = dataMapper.toRegistrar(payload);
        applicant.setId(UUID.randomUUID().toString());
        applicant.setIsRegistrar(true);
        smeCase.getApplicants().add(applicant);

        context.savePreliminaryCaseData(smeCase);
        return ActionResult.success(new CaseDataDto(context.getCaseKey()));
    }
}
