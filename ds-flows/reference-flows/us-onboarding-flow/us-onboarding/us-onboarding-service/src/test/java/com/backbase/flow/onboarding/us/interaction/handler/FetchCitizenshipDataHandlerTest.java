package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.NonResidentInfo;
import com.backbase.flow.application.uso.casedata.ResidencyAddress;
import com.backbase.flow.application.uso.casedata.W8ben;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.ResidencyAddressDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCitizenshipDataResponseDto;
import com.backbase.flow.utils.CaseDataUtils;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FetchCitizenshipDataHandlerTest {

    @Mock
    JourneyMapper<Applicant> mapper;

    @Mock
    CaseDataUtils caseDataUtils;

    @Mock
    InteractionContext interactionContext;

    @InjectMocks
    FetchCitizenshipDataHandler fetchCitizenshipDataHandler;

    @Test
    void handleFetchCitizenshipDataWithNonResidentDataFilled() {
        Void payload = null;

        interactionContext = mock(InteractionContext.class);

        var uuid = UUID.randomUUID();
        when(caseDataUtils.getOrCreateCaseKey(interactionContext))
            .thenReturn(uuid);

        when(mapper.read(null, null, uuid.toString()))
            .thenReturn(getCaseDataWithNonResidentData());

        ActionResult<FetchCitizenshipDataResponseDto> result = fetchCitizenshipDataHandler
            .handle(payload, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody().getType()).isEqualTo(CitizenshipType.NON_RESIDENT_ALIEN);
        assertThat(result.getBody().getSsn()).isNull();
        NonResidentDataDto nonResident = result.getBody().getNonResident();
        assertThat(nonResident).isNotNull();
        assertThat(nonResident.getCitizenshipCountryCode()).isEqualTo("Citizenship country code");

        ResidencyAddressDto residencyAddress = nonResident.getResidencyAddress();
        assertThat(residencyAddress).isNotNull();
        assertThat(residencyAddress.getCountryCode()).isEqualTo("Residency country code");
        assertThat(residencyAddress.getCity()).isEqualTo("City");
        assertThat(residencyAddress.getZipCode()).isEqualTo("Zip code");
        assertThat(residencyAddress.getNumberAndStreet()).isEqualTo("Number and street");

        assertThat(nonResident.getForeignTin()).isEqualTo("Foreign tin");
        assertThat(nonResident.getNationalTin()).isEqualTo("National tin");
        assertThat(nonResident.getWithholdingTaxAccepted()).isTrue();
        assertThat(nonResident.getW8benAccepted()).isTrue();
    }

    @Test
    void handleFetchCitizenshipDataWithSsnFilled() {
        Void payload = null;

        interactionContext = mock(InteractionContext.class);

        var uuid = UUID.randomUUID();
        when(caseDataUtils.getOrCreateCaseKey(interactionContext))
            .thenReturn(uuid);

        when(mapper.read(null, null, uuid.toString()))
            .thenReturn(getCaseDataWithSsn());

        ActionResult<FetchCitizenshipDataResponseDto> result = fetchCitizenshipDataHandler
            .handle(payload, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody().getType()).isEqualTo(CitizenshipType.US_CITIZEN);
        assertThat(result.getBody().getSsn()).isEqualTo("123456789");
        assertThat(result.getBody().getNonResident()).isNull();
    }


    @Test
    void handleFetchCitizenshipWithTypeChosen() {
        Void payload = null;

        interactionContext = mock(InteractionContext.class);

        var uuid = UUID.randomUUID();
        when(caseDataUtils.getOrCreateCaseKey(interactionContext))
            .thenReturn(uuid);

        when(mapper.read(null, null, uuid.toString()))
            .thenReturn(new Applicant().withCitizenship(new CitizenshipInfo()
                .withCitizenshipType(CitizenshipType.US_CITIZEN)));

        ActionResult<FetchCitizenshipDataResponseDto> result = fetchCitizenshipDataHandler
            .handle(payload, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody().getType()).isEqualTo(CitizenshipType.US_CITIZEN);
        assertThat(result.getBody().getSsn()).isNull();
        assertThat(result.getBody().getNonResident()).isNull();
    }

    @ParameterizedTest
    @MethodSource("applicantsWithEmptyCitizenship")
    void handleFetchCitizenshipEmptyData(Applicant applicant) {
        Void payload = null;

        interactionContext = mock(InteractionContext.class);

        var uuid = UUID.randomUUID();
        when(caseDataUtils.getOrCreateCaseKey(interactionContext))
            .thenReturn(uuid);

        when(mapper.read(null, null, uuid.toString()))
            .thenReturn(applicant);

        ActionResult<FetchCitizenshipDataResponseDto> result = fetchCitizenshipDataHandler
            .handle(payload, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody().getType()).isNull();
        assertThat(result.getBody().getSsn()).isNull();
        assertThat(result.getBody().getNonResident()).isNull();
    }

    @Test
    void shouldMatchName() {
        assertThat(fetchCitizenshipDataHandler.getName()).isEqualTo("fetch-citizenship-data");
    }

    private Applicant getCaseDataWithNonResidentData() {
        return new Applicant()
            .withCitizenship(new CitizenshipInfo()
                .withCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN)
                .withNonResident(new NonResidentInfo()
                    .withCitizenshipCountryCode("Citizenship country code")
                    .withResidencyAddress(new ResidencyAddress()
                        .withCountryCode("Residency country code")
                        .withCity("City")
                        .withZipCode("Zip code")
                        .withNumberAndStreet("Number and street")
                    )
                )
                .withForeignTin("Foreign tin")
                .withNationalTin("National tin")
                .withWithholdingTaxAccepted(true)
                .withW8ben(new W8ben().withAccepted(true).withAcceptedAt(OffsetDateTime.now()))
            );
    }

    private Applicant getCaseDataWithSsn() {
        return new Applicant().withCitizenship(
            new CitizenshipInfo().withCitizenshipType(CitizenshipType.US_CITIZEN).withSsn("123456789"));
    }

    private static Stream<Arguments> applicantsWithEmptyCitizenship() {
        return Stream.of(
            Arguments.of(new Applicant()),
            Arguments.of(new Applicant().withCitizenship(new CitizenshipInfo()))
        );
    }
}
