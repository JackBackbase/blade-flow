package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class TestBaseDocumentRequestsMilestoneProvider extends BaseDocumentRequestsMilestoneProvider {

    public TestBaseDocumentRequestsMilestoneProvider(CaseEngineEventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData) {
        return new TestCaseEvent(caseKey, "Test case event", Collections.emptyMap());
    }

    static class TestCaseEvent extends CaseEvent {

        public TestCaseEvent(UUID caseKey, String description, Map<String, String> metaData) {
            super(caseKey, description, metaData);
        }
    }
}
