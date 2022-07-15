package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.credential.CredentialService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class CheckCaseExistActionHandlerIT extends BaseIntegrationIT {

    private final String firstEmailAddress = "major@bb.com";
    private final SmeCaseDefinition firstCaseDefinition = new SmeCaseDefinition().withApplicants(
        List.of(new Applicant()
            .withIsRegistrar(true)
            .withFirstName("Major")
            .withLastName("Suchodolski")
            .withDateOfBirth(LocalDate.parse("1974-06-24"))
            .withEmail(firstEmailAddress)
            .withPhoneNumber("5555555")
        ));

    private final SmeCaseDefinition secondCaseDefinition = new SmeCaseDefinition().withApplicants(
        List.of(new Applicant()
            .withIsRegistrar(true)
            .withFirstName("Krzysztof")
            .withLastName("Kononowicz")
            .withDateOfBirth(LocalDate.parse("1963-01-21"))
            .withEmail("konon@bb.com")
            .withPhoneNumber("7654321")
        ));

    @Autowired
    private CredentialService credentialChecker;

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_caseExist_credentialExist() throws Exception {
        var interactionInstance = createInteractionInstanceInCaseStore(
            firstCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        doReturn(true).when(credentialChecker).exists(firstEmailAddress);

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("sme-onboarding-landing")))
            .andExpect(jsonPath("$.body.caseExist", Matchers.is(true)))
            .andExpect(jsonPath("$.body.identityCredentialExist", Matchers.is(true)));

        assertCaseDataForFirstCaseDefinition(interactionInstance.getCaseKey());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_caseExist_credentialNotExist() throws Exception {
        var interactionInstance = createInteractionInstanceInCaseStore(
            firstCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        doReturn(false).when(credentialChecker).exists(firstEmailAddress);

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("save-and-resume")))
            .andExpect(jsonPath("$.body.caseExist", Matchers.is(true)))
            .andExpect(jsonPath("$.body.identityCredentialExist", Matchers.is(false)));

        assertCaseDataForFirstCaseDefinition(interactionInstance.getCaseKey());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withUnknownInteraction_shouldReturnNotFound() throws Exception {
        var interactionInstance = createInteractionInstance(
            new SmeCaseDefinition(), TestConstants.SME_ONBOARDING_CHECK_CASE,
            UUID.randomUUID().toString()
        );

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(UUID.randomUUID().toString());
        interactionRequestDto.setPayload(null);

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_ANCHOR_DATA)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_caseNotExist() throws Exception {
        var interactionInstance = createInteractionInstance(
            firstCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString()
        );
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body.caseExist", Matchers.is(false)));

        assertCaseDataForFirstCaseDefinition(interactionInstance.getCaseKey());

        var caseFrom = getCase(interactionInstance.getCaseKey());
        assertThat(caseFrom.isPreliminary())
            .isFalse();
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_caseDoNotExist_firstNameNotMatch() throws Exception {
        createInteractionInstanceInCaseStore(
            secondCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString()
        );

        var interactionInstance = createInteractionInstance(
            firstCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString()
        );
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body.caseExist", Matchers.is(false)));

        var caseFrom = getCase(interactionInstance.getCaseKey());
        assertThat(caseFrom.isPreliminary())
            .isFalse();
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_caseNotExist_KYCPendingMessageEmitted() throws Exception {
        var interactionInstance = createInteractionInstance(
            firstCaseDefinition, TestConstants.SME_ONBOARDING_CHECK_CASE, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body.caseExist", Matchers.is(false)));

        var events = getEventsByName(interactionInstance.getCaseKey(), TestConstants.KYC_PENDING);

        assertThat(events).isNotEmpty();
        assertThat(events.get(0).getEventDescription()).isEqualTo("The message with name KYCPending was emitted");
    }

    private void assertCaseDataForFirstCaseDefinition(UUID caseKey) {
        var smeCase = getCaseData(caseKey);
        assertSoftly(softly -> {
            softly.assertThat(smeCase).isNotNull();
            softly.assertThat(smeCase.getApplicants().get(0)).isNotNull();
            softly.assertThat(smeCase.getApplicants().get(0).getFirstName()).isEqualTo("Major");
            softly.assertThat(smeCase.getApplicants().get(0).getLastName()).isEqualTo("Suchodolski");
            softly.assertThat(smeCase.getApplicants().get(0).getDateOfBirth()).isEqualTo(LocalDate.parse("1974-06-24"));
            softly.assertThat(smeCase.getApplicants().get(0).getEmail()).isEqualTo(firstEmailAddress);
        });
    }

}
