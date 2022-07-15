package com.backbase.flow.sme.onboarding.process.handler;

import com.backbase.flow.process.task.DefaultUserTaskHandler;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import org.springframework.stereotype.Component;

@Component("addressValidationIdtTaskHandler")
public class AddressValidationIdtTaskHandler extends DefaultUserTaskHandler<SmeCaseDefinition, Void, Void> {

}
