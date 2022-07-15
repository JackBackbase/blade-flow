package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.onboarding.us.mapper.citizenship.NonResidentDataMapper;
import com.backbase.flow.onboarding.us.process.dto.CitizenshipUserTaskInfoDto;
import com.backbase.flow.onboarding.us.process.dto.NonResidentDataReviewDto;
import com.backbase.flow.process.task.DefaultUserTaskHandler;
import com.backbase.flow.process.task.TaskContext;
import com.backbase.flow.process.task.TaskResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component("verifyNonResidentDataUserTaskHandler")
public class VerifyNonResidentDataUserTaskHandler extends
    DefaultUserTaskHandler<Onboarding, CitizenshipUserTaskInfoDto, NonResidentDataReviewDto> {

    public static final String REVIEW_RESULT = "citizenshipDataApproved";
    private final JourneyMapper<Applicant> journeyMapper;
    private final NonResidentDataMapper nonResidentDataMapper;

    @Override
    public TaskResult<CitizenshipUserTaskInfoDto> complete(TaskContext<Onboarding> taskContext,
        NonResidentDataReviewDto input) {
        Applicant applicant = journeyMapper.read(null, null, taskContext.getCase());
        CitizenshipReview review = applicant.getCitizenship().getCitizenshipReview();
        review.setApproved(input.getApproved());
        review.setComment(input.getComment());

        taskContext.setProcessVariable(REVIEW_RESULT, input.getApproved());

        journeyMapper.write(applicant, null, null, taskContext.getCase());
        CitizenshipUserTaskInfoDto citizenshipUserTaskInfoDto = nonResidentDataMapper.fromApplicant(applicant);
        return new TaskResult<>(citizenshipUserTaskInfoDto);
    }

    @Override
    public CitizenshipUserTaskInfoDto getViewData(TaskContext<Onboarding> taskContext) {
        Applicant applicant = journeyMapper.read(null, null, taskContext.getCase());
        return nonResidentDataMapper.fromApplicant(applicant);
    }
}
