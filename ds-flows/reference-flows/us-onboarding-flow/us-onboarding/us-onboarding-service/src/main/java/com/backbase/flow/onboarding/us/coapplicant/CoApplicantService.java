package com.backbase.flow.onboarding.us.coapplicant;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.interaction.engine.data.model.StepInstance;
import com.backbase.flow.service.resume.stepfinder.StepFinder;
import com.backbase.flow.service.resume.util.CaseSearch;
import com.backbase.flow.service.resume.util.ResumeUtils;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoApplicantService {

    private static final String SUCCESSFULLY_DONE_CO_APPLICANT = "successfully-done-co-applicant";
    private final ResumeUtils resumeUtils;
    private final FlowSecurityContext flowSecurityContext;
    private final StepFinder stepFinder;

    public Optional<Case> resumeCoApplicantCaseAndInteractionStep(InteractionContext context, UUID coApplicantId) {

        final Optional<Case> previousCaseOptional = getExistingCaseByCoApplicantId(coApplicantId);

        if (previousCaseOptional.isEmpty()) {
            log.debug("Not resuming because no mainApplicant's case was found");
            return Optional.empty();
        }
        final Case previousCase = previousCaseOptional.get();

        final Optional<InteractionInstance> previousInteractionOptional = flowSecurityContext
            .runWithoutAuthorization(() -> resumeUtils.getLatestInteractionInstance(previousCase.getKey()));

        if (previousInteractionOptional.isEmpty()) {
            log.debug("Not resuming because no mainApplicant's interaction was found");
            return Optional.empty();
        }
        final InteractionInstance previousInteraction = previousInteractionOptional.get();

        resumeUtils.swapUnderlyingCase(context.getInteractionId(), previousCase.getKey(),
            previousCase.getVersion());
        //Checking if it's a last step of the mainApplicant. If it's not — resume
        if (!SUCCESSFULLY_DONE_CO_APPLICANT.equals(previousInteraction.getCurrentStep().getName())) {
            log.debug("Resuming step, because it is a coApplicant's interaction");
            Optional<InteractionInstance> interactionOptional = resumeUtils
                .getLatestInteractionInstance(context.getCaseKey());
            if (interactionOptional.isEmpty()) {
                return previousCaseOptional;
            }
            final StepInstance resumeToStep = stepFinder
                .getResumeToStep(interactionOptional.get(), previousInteraction);
            if (resumeToStep != null) {
                resumeUtils
                    .setStepForInteraction(context.getInteractionId(), resumeToStep);
            }
        }

        return previousCaseOptional;
    }

    public Optional<Case> getExistingCaseByCoApplicantId(final UUID coApplicantId) {
        CaseSearch caseSearch = new CaseSearch("coApplicantId", coApplicantId.toString(), null);
        return resumeUtils.getExistingCaseBySpecifiedField(caseSearch);
    }

}
