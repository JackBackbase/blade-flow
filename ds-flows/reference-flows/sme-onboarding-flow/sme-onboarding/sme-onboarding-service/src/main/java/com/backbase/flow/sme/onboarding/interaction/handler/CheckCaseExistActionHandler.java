package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.cases.CaseFilterRequest;
import com.backbase.flow.casedata.definition.CaseDefinitionId;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.process.service.FlowProcessService;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import com.backbase.flow.sme.onboarding.credential.CredentialService;
import com.backbase.flow.sme.onboarding.interaction.model.CaseResponseDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("sme-onboarding-check-case-exist")
public class CheckCaseExistActionHandler extends ValidationActionHandler<Void, CaseResponseDto> {

    private final CaseDataService caseDataService;
    private final FlowSecurityContext flowSecurityContext;
    private final FlowProcessService processService;
    private final CredentialService credentialChecker;

    @Override
    protected ActionResult<CaseResponseDto> handleValidData(
        Void payload, SmeCaseDefinition smeCase, InteractionContext context
    ) {
        var applicant = SmeUtils.getRegistrar(smeCase);
        var response = new CaseResponseDto();
        var cases = getCases(applicant.getEmail());
        for (Case caseFromCaseStore : cases) {
            var storedSmeCase = caseFromCaseStore.getCaseData(SmeCaseDefinition.class);
            var storedApplicant = SmeUtils.getRegistrar(storedSmeCase);
            if (applicantsMatch(storedApplicant, applicant)) {
                response.setCaseExist(true);
                response.setCaseKey(caseFromCaseStore.getKey().toString());
                response.setIdentityCredentialExist(credentialChecker.exists(applicant.getEmail()));
                return ActionResult.success(response);
            } else {
                flowSecurityContext.runWithoutAuthorization(() ->
                    caseDataService.archiveCase(caseFromCaseStore.getKey()));
            }
        }
        context.promoteCase();
        response.setFirstName(applicant.getFirstName());
        response.setLastName(applicant.getLastName());
        flowSecurityContext.runWithoutAuthorization(() -> processService.startProcess(
            context.getCaseKey(), ProcessConstants.BPM_DATA_GATHERING)
        );
        return ActionResult.success(response);
    }

    private List<Case> getCases(String email) {
        var caseFilterRequest = new CaseFilterRequest()
            .setPropertyName(CaseDefinitionConstants.EMAIL_INDEX)
            .setPropertyValues(List.of(email));
        return flowSecurityContext.runWithoutAuthorization(() -> caseDataService.filterCasesByCaseDefinitionId(
            new CaseDefinitionId(CaseDefinitionConstants.CASE_DEFINITION_KEY),
            caseFilterRequest
        ));
    }

    private boolean applicantsMatch(Applicant storedApplicant, Applicant applicant) {
        return StringUtils.equals(storedApplicant.getFirstName(), applicant.getFirstName())
            && StringUtils.equals(storedApplicant.getLastName(), applicant.getLastName())
            && StringUtils.equals(storedApplicant.getDateOfBirth().toString(), applicant.getDateOfBirth().toString())
            && StringUtils.equals(storedApplicant.getPhoneNumber(), applicant.getPhoneNumber());
    }
}
