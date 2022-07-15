package com.backbase.flow.sme.onboarding.interaction.hook;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.interaction.model.TaskDto;
import com.backbase.flow.sme.onboarding.interaction.service.TaskCompletionService;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("complete-address-validation-idt")
public class CompleteAddressValidationIdtHook extends CompleteApplicantTaskHook {

    @Getter
    @SuppressWarnings("squid:S1170")
    private final String taskDefinitionKey = "address-validation-idt";

    public CompleteAddressValidationIdtHook(FlowSecurityContext flowSecurityContext,
                                            TaskCompletionService taskCompletionService) {
        super(flowSecurityContext, taskCompletionService);
    }

    @Override
    boolean isTaskReadyToClose(TaskDto taskDto, SmeCaseDefinition smeCase) {
        return SmeUtils.isAddressValidatedForApplicant(smeCase, taskDto.getApplicantId());
    }
}
