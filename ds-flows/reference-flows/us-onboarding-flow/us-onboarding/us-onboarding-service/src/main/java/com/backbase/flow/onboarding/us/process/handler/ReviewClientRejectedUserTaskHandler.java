package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.onboarding.us.process.dto.ReviewClientRejectedDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.onboarding.us.util.OnboardingMapper;
import com.backbase.flow.process.task.DefaultUserTaskHandler;
import com.backbase.flow.process.task.TaskContext;
import com.backbase.flow.process.task.TaskResult;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component("reviewClientRejectedUserTaskHandler")
public class ReviewClientRejectedUserTaskHandler extends
    DefaultUserTaskHandler<Onboarding, ReviewClientRejectedDto, Map<String, Boolean>> {

    private final CaseDataService caseDataService;
    private final FlowSecurityContext flowSecurityContext;

    @Override
    public TaskResult<ReviewClientRejectedDto> complete(TaskContext<Onboarding> taskContext) {
        return new TaskResult<>(getViewData(taskContext));
    }

    @Override
    public TaskResult<ReviewClientRejectedDto> complete(TaskContext<Onboarding> taskContext,
        Map<String, Boolean> input) {
        final Onboarding data = taskContext.getCaseDataAsDTO();
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(data);
        applicant.setCustomerApproved(input.get("customerApproved"));
        OnboardingCaseDataUtils.setApplicant(data, applicant);
        taskContext.getCase().setCaseData(data);
        flowSecurityContext.runWithoutAuthorization(() -> {
            caseDataService.updateCase(taskContext.getCase());
            return null;
        });
        return new TaskResult<>(OnboardingMapper.MAPPER.mapForUserTask(applicant));
    }

    @Override
    public ReviewClientRejectedDto getViewData(TaskContext<Onboarding> taskContext) {
        final Onboarding data = taskContext.getCaseDataAsDTO();
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(data);
        return OnboardingMapper.MAPPER.mapForUserTask(applicant);
    }

}