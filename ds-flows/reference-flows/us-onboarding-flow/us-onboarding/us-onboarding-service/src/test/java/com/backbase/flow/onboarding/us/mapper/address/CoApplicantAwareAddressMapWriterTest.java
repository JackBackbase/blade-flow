package com.backbase.flow.onboarding.us.mapper.address;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import com.backbase.flow.service.mapper.AddressConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoApplicantAwareAddressMapWriterTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final CoApplicantAwareAddressMapWriter writer = new CoApplicantAwareAddressMapWriter(objectMapper,
        caseDataService);

    @BeforeEach
    void setup() {
        when(caseDataService.updateCase(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void write_noMainApplicantCompletionId_writesToMainApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();
        final Map<String, Object> journeyData = Map.of("numberAndStreet", "A number and a street");

        final Case updatedCase = writer.write(journeyData, null, AddressConstants.MAPPER_BASE_CONTEXT, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getMainApplicant().getFirstName()).isEqualTo("main-applicant");
        assertThat(updatedCaseData.getMainApplicant().getAddress().getNumberAndStreet()).isEqualTo("A number and a street");
        assertThat(updatedCaseData.getCoApplicant()).isNull();
    }

    @Test
    void write_hasMainApplicantCompletionId_writesToCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();
        final Map<String, Object> journeyData = Map.of("numberAndStreet", "A number and a street");

        final Case updatedCase = writer.write(journeyData, null, AddressConstants.MAPPER_BASE_CONTEXT, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getCoApplicant().getFirstName()).isEqualTo("co-applicant");
        assertThat(updatedCaseData.getCoApplicant().getAddress().getNumberAndStreet())
            .isEqualTo("A number and a street");
        assertThat(updatedCaseData.getMainApplicant().getAddress()).isNull();
    }
}
