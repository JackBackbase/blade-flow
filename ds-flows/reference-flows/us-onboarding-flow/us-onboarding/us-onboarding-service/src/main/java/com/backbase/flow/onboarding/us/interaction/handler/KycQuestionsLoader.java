package com.backbase.flow.onboarding.us.interaction.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("kyc-questions-loader")
public class KycQuestionsLoader implements ActionHandler<Void, String> {

    private final JourneyMapper<Onboarding> journeyMapper;

    @Override
    public ActionResult<String> handle(Void payload, InteractionContext context) {
        try {
            final Onboarding onboarding = journeyMapper.read(null, null, context.getCaseKey().toString());
            final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);
            String kycName =
                applicant.getAntiMoneyLaunderingInfo().getAmlResult().getMatchStatus().equalsIgnoreCase("no_match")
                ? "kyc/kyc.json"
                : "kyc/kyc2.json";
            StringWriter writer = new StringWriter();
            IOUtils.copy(new ClassPathResource(kycName).getInputStream(), writer, StandardCharsets.UTF_8);
            String json = writer.toString().replace("\n", "");
            return new ActionResult<>(json);
        } catch (Exception e) {
            return new ActionResult<>(null, Errors.buildError("", e.getMessage()));
        }
    }
}
