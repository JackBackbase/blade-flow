package com.backbase.flow.onboarding.us.helpers.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import java.util.Map;

public class CoApplicantWelcomeAssertions extends BaseAssertions {

    public static void assertFetchCoApplicantData(InteractionResponseDto response, String caseKey) {
        assertThat(response.getStep().getStatus()).isNull();
        assertThat(response.getActionErrors().size()).isZero();
        final Onboarding onboarding = getCaseData(caseKey);
        Map<String, Object> mapResponse = (Map<String, Object>) response.getBody();
        assertThat((Map<String, Object>) mapResponse.get("coApplicant"))
            .containsEntry("firstName", onboarding.getCoApplicant().getFirstName());
        assertThat((Map<String, Object>) mapResponse.get("coApplicant"))
            .containsEntry("lastName", onboarding.getCoApplicant().getLastName());
        //todo add when JOURNEYS-2014 will be completed
        //assertThat((Map<String,Object>) mapResponse.get("coApplicant")).containsEntry("firstName", onboarding.getCoApplicant().getFirstName());
    }

}
