package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.process.task.DefaultUserTaskHandler;
import org.springframework.stereotype.Component;

@Component("kycIdtTaskHandler")
public class KycIdtTaskHandler extends DefaultUserTaskHandler<Onboarding, Void, Void> {

}
