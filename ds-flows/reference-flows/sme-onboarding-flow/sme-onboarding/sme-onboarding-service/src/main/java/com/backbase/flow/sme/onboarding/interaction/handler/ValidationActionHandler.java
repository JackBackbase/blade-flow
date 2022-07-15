package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.action.validation.ActionHandlerPayloadValidator;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.validation.util.ValidationUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The base action handler with validation check on payload and business type.
 *
 * @param <P> payload type
 * @param <R> body type
 */
@RequiredArgsConstructor
public abstract class ValidationActionHandler<P, R> implements ActionHandler<P, R> {

    @Autowired
    private ValidationUtil validationUtil;

    /**
     * Provide validation logic on the the payload and validate process on case data against business type.
     *
     * @param payload payload to be processed
     * @param context interaction context
     * @return the action result and its body.
     */
    public ActionResult<R> handle(P payload, InteractionContext context) {
        SmeCaseDefinition smeCase;

        if (Objects.isNull(context.getCaseKey())) {
            smeCase = new SmeCaseDefinition();
        } else {
            smeCase = context.getCaseData(SmeCaseDefinition.class);
        }

        if (Objects.isNull(payload)) {
            return handleValidData(null, smeCase, context);
        }

        final var validator = new ActionHandlerPayloadValidator<>(validationUtil);
        final var violations = validator.validate(payload);

        if (!violations.isEmpty()) {
            return new ActionResult<>(null, violations);
        }

        return handleValidData(payload, smeCase, context);
    }

    protected abstract ActionResult<R> handleValidData(
        P payload, SmeCaseDefinition smeCase, InteractionContext context
    );

}
