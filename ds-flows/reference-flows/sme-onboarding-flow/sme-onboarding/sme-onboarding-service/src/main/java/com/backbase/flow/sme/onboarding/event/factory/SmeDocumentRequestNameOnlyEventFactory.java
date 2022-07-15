package com.backbase.flow.sme.onboarding.event.factory;

import static com.backbase.flow.sme.onboarding.event.AllDocsVerifiedEvent.ALL_DOCS_VERIFIED_EVENT_NAME;
import static com.backbase.flow.sme.onboarding.event.ApplicationCompleteEvent.APPLICATION_COMPLETE_EVENT_NAME;
import static com.backbase.flow.sme.onboarding.event.NoDocsRequiredEvent.NO_DOCS_REQUIRED_EVENT_NAME;

import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.process.events.DefaultNameOnlyEvent;
import com.backbase.flow.process.events.NameOnlyEvent;
import com.backbase.flow.process.events.NameOnlyEventFactory;
import com.backbase.flow.sme.onboarding.event.AllDocsVerifiedEvent;
import com.backbase.flow.sme.onboarding.event.ApplicationCompleteEvent;
import com.backbase.flow.sme.onboarding.event.NoDocsRequiredEvent;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

@NoArgsConstructor
@Primary
public class SmeDocumentRequestNameOnlyEventFactory implements NameOnlyEventFactory {

    @RunWithoutAuthorization
    @Override
    public NameOnlyEvent create(UUID caseKey, String processInstanceId, String eventName) {

        switch (eventName) {
            case ALL_DOCS_VERIFIED_EVENT_NAME:
                return new AllDocsVerifiedEvent(caseKey);

            case APPLICATION_COMPLETE_EVENT_NAME:
                return new ApplicationCompleteEvent(caseKey);

            case NO_DOCS_REQUIRED_EVENT_NAME:
                return new NoDocsRequiredEvent(caseKey);

            default:
                return new DefaultNameOnlyEvent(caseKey, processInstanceId, eventName);
        }
    }
}
