package com.backbase.flow.onboarding.us.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.action.validation.HandlerPayloadValidator;

/**
 * This class provide around validation logic.
 *
 * @param <P> payload type
 * @param <R> body type
 */
public abstract class ActionHandlerWithValidation<P, R> implements ActionHandler<P, R> {

    /**
     * Provide around validation logic on the the payload.
     *
     * @param payload payload to be processed
     * @param context interaction context
     * @return the action result and its body.
     */
    public ActionResult<R> handle(P payload, InteractionContext context) {
        final HandlerPayloadValidator<P> validator = new HandlerPayloadValidator<>();
        final Errors violations = validator.validate(payload);

        if (!violations.isEmpty()) {
            return new ActionResult<>(null, violations);
        }

        return handleWithValidation(payload, context);
    }

    protected abstract ActionResult<R> handleWithValidation(P payload, InteractionContext context);
}