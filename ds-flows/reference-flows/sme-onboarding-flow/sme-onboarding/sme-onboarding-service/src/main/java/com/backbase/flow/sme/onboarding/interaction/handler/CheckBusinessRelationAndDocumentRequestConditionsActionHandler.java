package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.interaction.model.BusinessRelationAndDocumentRequestConditionsResponseDto;
import com.backbase.flow.sme.onboarding.process.service.SmeBusinessRelationService;
import com.backbase.flow.sme.onboarding.process.service.SmeDocumentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("check-business-relation-and-document-requests-conditions")
@RequiredArgsConstructor
public class CheckBusinessRelationAndDocumentRequestConditionsActionHandler extends
    ValidationActionHandler<Void, BusinessRelationAndDocumentRequestConditionsResponseDto> {

    private final SmeBusinessRelationService smeBusinessRelationService;
    private final SmeDocumentRequestService smeDocumentRequestService;

    @Override
    protected ActionResult<BusinessRelationAndDocumentRequestConditionsResponseDto> handleValidData(
        Void payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        var isBusinessRelationRequired = smeBusinessRelationService.isBusinessRelationRequired(smeCase, context);
        var isDocumentRequired = smeDocumentRequestService.isDocumentRequired(context);
        smeCase.setDocumentRequired(isDocumentRequired);
        smeCase.setBusinessRelationRequired(isBusinessRelationRequired);
        context.saveCaseDataToReadCaseData(smeCase);

        return new ActionResult<>(
            new BusinessRelationAndDocumentRequestConditionsResponseDto(
                isBusinessRelationRequired,
                isDocumentRequired
            ));
    }
}
