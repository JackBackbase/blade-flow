package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.api.model.FlowStatusTrackingDto;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.interaction.model.CaseMilestoneRequestDto;
import com.backbase.flow.sme.onboarding.mapper.DataMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("get-milestones")
public class CaseMilestonesActionHandler implements ActionHandler<CaseMilestoneRequestDto, FlowStatusTrackingDto> {

    private final CaseDataService caseDataService;
    private final FlowSecurityContext flowSecurityContext;
    private final DataMapper dataMapper;

    @Override
    public ActionResult<FlowStatusTrackingDto> handle(CaseMilestoneRequestDto payload, InteractionContext context) {
        var caseMilestones = flowSecurityContext.runWithoutAuthorization(
            () -> caseDataService.getCaseMilestones(context.getCaseKey(), payload.getEpic())
        );

        var responseDto = new FlowStatusTrackingDto();

        if (caseMilestones != null) {
            var milestones = caseMilestones.stream()
                .map(dataMapper::toFlowMilestoneDto)
                .collect(Collectors.toList());

            responseDto.setMilestones(milestones);
        }

        return new ActionResult<>(responseDto);
    }
}
