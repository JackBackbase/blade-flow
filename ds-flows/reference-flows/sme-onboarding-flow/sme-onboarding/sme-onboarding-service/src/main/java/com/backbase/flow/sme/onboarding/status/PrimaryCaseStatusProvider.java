package com.backbase.flow.sme.onboarding.status;

import com.backbase.flow.casedata.CaseDefinitionsProperties;
import com.backbase.flow.casedata.cases.CaseRepository;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.casedata.status.StatusProvider;
import com.backbase.flow.casedata.status.events.CaseCreatedEvent;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrimaryCaseStatusProvider extends StatusProvider {

    protected PrimaryCaseStatusProvider(
        CaseDefinitionsProperties caseDefinitionProperties,
        CaseRepository caseRepository
    ) {
        super(caseDefinitionProperties, caseRepository);
    }

    @Override
    protected List<Class<? extends CaseEvent>> getProcessableEventClasses() {
        List<Class<? extends CaseEvent>> processableEvents = new ArrayList<>();
        processableEvents.add(CaseCreatedEvent.class);
        return processableEvents;
    }

    @Override
    protected String determineCaseStatusValue(CaseEvent caseEvent) {
        return caseEvent instanceof CaseCreatedEvent ? "open" : null;
    }
}
