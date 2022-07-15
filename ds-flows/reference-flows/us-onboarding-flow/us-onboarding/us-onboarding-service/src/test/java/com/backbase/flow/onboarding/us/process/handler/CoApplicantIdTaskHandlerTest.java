package com.backbase.flow.onboarding.us.process.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.exception.HandlerException;
import com.backbase.flow.onboarding.us.service.coapplicant.CoApplicantNotificationService;
import com.backbase.flow.onboarding.us.service.email.SendGridService;
import com.backbase.flow.test.casedata.FakeDelegateExecution;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CoApplicantIdTaskHandlerTest {

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final CoApplicantNotificationService coApplicantNotificationService = mock(CoApplicantNotificationService.class);
    private final CoApplicantIdTaskHandler handler = new CoApplicantIdTaskHandler(caseDataService, coApplicantNotificationService);


    @Test
    void execute_setsCoApplicantId() throws HandlerException {
        var caseKey = UUID.randomUUID();
        var aCase = new Case();
        aCase.setCaseData(new Onboarding());
        when(caseDataService.getCaseByKey(caseKey))
            .thenReturn(aCase);

        var execution = new FakeDelegateExecution(caseKey);

        handler.execute(execution);

        var caseCaptor = ArgumentCaptor.forClass(Case.class);
        verify(caseDataService).updateCase(caseCaptor.capture());
        var onboarding = caseCaptor.getValue().getCaseData(Onboarding.class);
        assertThat(onboarding.getCoApplicantId()).isNotNull();
    }
}
