package com.backbase.flow.sme.onboarding.process;

import static com.backbase.flow.sme.onboarding.TestConstants.AML_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.AML_SUCCEED_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ACTION_NOT_REQUIRED_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_FINISHED_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.KYC_COMPLETED;
import static com.backbase.flow.sme.onboarding.TestConstants.KYC_PENDING;
import static com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants.APPLICANT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData.Status;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.IdentityVerificationResult;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.casedata.TermsAndConditions;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import com.backbase.flow.test.interaction.FakeInteractionContext;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class KycProcessIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void startProcess_whenSingleApplicantApplied_shouldFinishKycProcessSuccessfully() {
        //given
        var applicantId = UUID.randomUUID();
        var caseDef = new SmeCaseDefinition()
            .withCompanyLookupInfo(
                new CompanyLookupInfo().withBusinessStructureInfo(
                    new BusinessStructureInfo().withType(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP)
                ))
            .withApplicants(List.of(getMainApplicant(applicantId)))
            .withBusinessRelationRequired(false)
            .withTermsAndConditions(getTermsAndConditions())
            .withIdentityVerificationResult(getIdentityVerificationResult());
        var exCase = startCase(caseDef);
        var fakeInteractionContext = new FakeInteractionContext(exCase);

        //when
        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYC);
        completeAddressValidationIdtHook.executeHook(fakeInteractionContext);
        completeIdvIdtHook.executeHook(fakeInteractionContext);

        //then
        var caseData = caseDataService.getCaseByKey(exCase.getKey()).getCaseData(SmeCaseDefinition.class);
        var amlInfos = caseData.getApplicants().stream().map(Applicant::getAntiMoneyLaunderingInfo)
            .collect(Collectors.toList());

        var applicantOnboardingPendingEvents = getEventsByName(exCase.getKey(), APPLICANT_ONBOARDING_PENDING_EVENT);
        var applicantOnboardingFinishedEvents = getEventsByName(exCase.getKey(), APPLICANT_ONBOARDING_FINISHED_EVENT);

        assertSoftly(softly -> {
            softly.assertThat(
                    containsAllEvents(exCase.getKey(),
                        List.of(
                            KYC_PENDING,
                            APPLICANT_ONBOARDING_PENDING_EVENT,
                            AML_PENDING_EVENT,
                            AML_SUCCEED_EVENT,
                            APPLICANT_ONBOARDING_FINISHED_EVENT,
                            KYC_COMPLETED
                        )))
                .isTrue();
            assertThat(applicantOnboardingPendingEvents.get(0).getMetadata())
                .extracting(m -> m.get(APPLICANT_ID))
                .isEqualTo(applicantId.toString());
            assertThat(applicantOnboardingFinishedEvents.get(0).getMetadata())
                .containsEntry(APPLICANT_ID, applicantId.toString());
            softly.assertThat(amlInfos)
                .isNotEmpty();
            softly.assertThat(AmlInfo.Status.SUCCESS)
                .isEqualTo(amlInfos.get(0).getStatus());
        });
    }

    @ParameterizedTest
    @MethodSource("businessRelationPossibleStatuses")
    @WithFlowAnonymousUser
    void startProcess_whenMultipleApplicantsApplied_shouldRunKycProcessDependsOnBusinessRelationStatus(
        BusinessRelationsCaseData businessRelationsCaseData,
        List<String> expectedEvents
    ) {
        //given
        var caseDef = new SmeCaseDefinition()
            .withCompanyLookupInfo(
                new CompanyLookupInfo().withBusinessStructureInfo(
                    new BusinessStructureInfo().withType(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP)
                ))
            .withApplicants(getMultipleApplicants(getMainApplicant(UUID.randomUUID())))
            .withBusinessRelationRequired(true)
            .withBusinessRelations(businessRelationsCaseData)
            .withTermsAndConditions(getTermsAndConditions())
            .withIdentityVerificationResult(getIdentityVerificationResult());
        var exCase = startCase(caseDef);
        var fakeInteractionContext = new FakeInteractionContext(exCase);

        //when
        flowProcessService.startProcess(exCase.getKey(), ProcessConstants.BPM_KYC);
        completeAddressValidationIdtHook.executeHook(fakeInteractionContext);
        completeIdvIdtHook.executeHook(fakeInteractionContext);

        //then
        assertThat(
            containsAllEvents(exCase.getKey(), expectedEvents)).isTrue();
    }

    private static Stream<Arguments> businessRelationPossibleStatuses() {
        return Stream.of(
            Arguments.of(
                new BusinessRelationsCaseData().withStatus(Status.IN_REVIEW),
                List.of(KYC_PENDING,
                    APPLICANT_ONBOARDING_PENDING_EVENT,
                    AML_PENDING_EVENT,
                    AML_SUCCEED_EVENT,
                    APPLICANT_ONBOARDING_FINISHED_EVENT)
            ),
            Arguments.of(
                new BusinessRelationsCaseData().withStatus(Status.COMPLETE),
                List.of(KYC_PENDING,
                    APPLICANT_ONBOARDING_PENDING_EVENT,
                    AML_PENDING_EVENT,
                    AML_SUCCEED_EVENT,
                    APPLICANT_ONBOARDING_FINISHED_EVENT,
                    APPLICANT_ACTION_NOT_REQUIRED_EVENT,
                    KYC_COMPLETED)
            )
        );
    }

    private List<Applicant> getMultipleApplicants(final Applicant mainApplicant) {
        var businessPerson = new Applicant()
            .withId(UUID.randomUUID().toString())
            .withFirstName("name")
            .withLastName("lastname")
            .withDateOfBirth(LocalDate.now().minusYears(20))
            .withEmail("email@test.invalid")
            .withRelationType(Applicant.RelationType.CONTROL_PERSON)
            .withRole("CFO")
            .withControlPerson(true)
            .withIsRegistrar(true)
            .withOwnershipPercentage(65.0)
            .withPersonalAddress(new Address()
                .withCity("Phoenix")
                .withState("AZ")
                .withZipCode("11111")
                .withApt("14")
                .withNumberAndStreet("Street")
            );

        var nextBusinessPerson = new Applicant()
            .withId(UUID.randomUUID().toString())
            .withFirstName("FirstName")
            .withLastName("Lastname")
            .withDateOfBirth(LocalDate.now().minusYears(20))
            .withEmail("some-email@test.invalid")
            .withRelationType(Applicant.RelationType.CONTROL_PERSON)
            .withRole("CFO")
            .withControlPerson(false)
            .withIsRegistrar(false)
            .withOwnershipPercentage(10.0)
            .withPersonalAddress(new Address()
                .withCity("Phoenix")
                .withState("AZ")
                .withZipCode("11111")
                .withApt("14")
                .withNumberAndStreet("Street")
            );

        return List.of(mainApplicant, businessPerson, nextBusinessPerson);
    }

    private TermsAndConditions getTermsAndConditions() {
        return new TermsAndConditions()
            .withAccepted(true)
            .withVersion("1")
            .withAcceptanceDate(OffsetDateTime.now());
    }

    private IdentityVerificationResult getIdentityVerificationResult() {
        return new IdentityVerificationResult()
            .withAdditionalProperty("verificationId", "approved")
            .withAdditionalProperty("documentStatus", "documentStatus")
            .withAdditionalProperty("filesetNameSuffix", "filesetNameSuffix");
    }
}
