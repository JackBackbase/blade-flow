package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.tracking.CaseMilestone;
import com.backbase.flow.casedata.tracking.CaseMilestoneId;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.interaction.model.CaseMilestoneRequestDto;
import com.backbase.flow.sme.onboarding.mapper.DataMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CaseMilestonesActionHandlerTest {

    public static final String MAIN = "Main";

    private CaseMilestonesActionHandler actionHandler;
    private InteractionContext context;

    @BeforeEach
    void setUp() {
        var caseDataService = Mockito.mock(CaseDataService.class);

        var flowSecurityContext = Mockito.mock(FlowSecurityContext.class);

        var dataMapper = Mockito.mock(DataMapper.class);


        var caseKey = UUID.randomUUID();
        var epic = MAIN;

        var milestones = List.of(
            createMilestone(caseKey, epic, "Application submitted"),
            createMilestone(caseKey, epic, "Documents Uploaded"),
            createMilestone(caseKey, epic, "Documents reviewed"),
            createMilestone(caseKey, epic, "Application completed")
        );

        when(flowSecurityContext.runWithoutAuthorization(any(Callable.class)))
            .thenAnswer(invocationOnMock -> ((Callable) invocationOnMock.getArgument(0)).call());

        when(caseDataService.getCaseMilestones(any(), any()))
            .thenReturn(milestones);

        context = mock(InteractionContext.class);
        when(context.getInteractionId()).thenReturn(UUID.randomUUID());
        when(context.getCaseKey()).thenReturn(caseKey);

        actionHandler = new CaseMilestonesActionHandler(caseDataService, flowSecurityContext, dataMapper);
    }

    private CaseMilestone createMilestone(UUID caseKey, String epic, String name) {
        var milestone = new CaseMilestone();
        milestone.setCaseMilestoneId(new CaseMilestoneId(caseKey, epic, name));
        milestone.setCreatedAt(OffsetDateTime.now());
        return milestone;
    }

    @Test
    void handle_milestonesExist_shouldReturnListOfMilestones() {
        var payload = new CaseMilestoneRequestDto();
        payload.setEpic(MAIN);

        var result = actionHandler.handle(payload, context);
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMilestones()).isNotNull();
        assertThat(result.getBody().getMilestones().isEmpty()).isFalse();
    }
}
