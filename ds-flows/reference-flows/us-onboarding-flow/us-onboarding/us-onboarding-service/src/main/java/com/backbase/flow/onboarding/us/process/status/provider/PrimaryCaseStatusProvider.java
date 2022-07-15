package com.backbase.flow.onboarding.us.process.status.provider;

import com.backbase.flow.casedata.CaseDefinitionsProperties;
import com.backbase.flow.casedata.cases.CaseRepository;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.casedata.status.StatusProvider;
import com.backbase.flow.casedata.status.events.CaseCreatedEvent;
import com.backbase.flow.onboarding.us.process.status.event.CaseClosedEvent;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrimaryCaseStatusProvider extends StatusProvider {


    protected PrimaryCaseStatusProvider(
        CaseDefinitionsProperties caseDefinitionProperties,
        CaseRepository caseRepository) {
        super(caseDefinitionProperties, caseRepository);
    }

    @Override
    protected List<Class<? extends CaseEvent>> getProcessableEventClasses() {
        List<Class<? extends CaseEvent>> processableEvents = new ArrayList<>();
        processableEvents.add(CaseCreatedEvent.class);
        processableEvents.add(CaseClosedEvent.class);
        return processableEvents;
    }

    @Override
    protected String determineCaseStatusValue(CaseEvent caseEvent) {
        if (caseEvent instanceof CaseCreatedEvent) {
            return "open";
        } else if (caseEvent instanceof CaseClosedEvent) {
            return "closed";
        }
        return null;
    }

}