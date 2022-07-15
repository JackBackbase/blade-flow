package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.ResidencyAddressDto;
import com.backbase.flow.onboarding.us.mapper.citizenship.NonResidentDataMapperImpl;
import com.backbase.flow.onboarding.us.service.CountryService;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class SubmitNonResidentDataHandlerTest {

    public static final String VALID_NATIONAL_TIN = "bbb321321BBB!";
    public static final String VALID_FOREIGN_TIN = "aaa123123AAA!";
    public static final String VALID_CODE_COUNTRY_2 = "BBB";
    public static final String VALID_CODE_COUNTRY_3 = "CCC";
    public static final String VALID_CITY = "Some-City Example";
    public static final String VALID_STREET = "12 Street-somewhere";
    public static final String VALID_ZIP_CODE = "12-Example Zip";
    public static final String INVALID_COUNTRY_CODE = "Invalid Value";
    public static final String VALID_CODE_COUNTRY_1 = "AAA";

    private final InteractionContext interactionContext = Mockito.mock(InteractionContext.class);
    private final CaseDataUtils caseDataUtils = mock(CaseDataUtils.class);
    @SuppressWarnings("unchecked")
    private final JourneyMapper<Onboarding> mapper = mock(JourneyMapper.class);
    private final CountryService countryService = mock(CountryService.class);
    private final NonResidentDataMapperImpl nonResidentDataMapper = mock(NonResidentDataMapperImpl.class);
    private final UUID caseKey = UUID.randomUUID();
    ArgumentCaptor<Onboarding> onboardingCaptor = ArgumentCaptor.forClass(Onboarding.class);

    private final SubmitNonResidentDataHandler handler = new SubmitNonResidentDataHandler(
        mapper, caseDataUtils, countryService, nonResidentDataMapper);

    @Test
    void applicantWithNoCitizenship_ReturnsError() {
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant());

        mockedServicesCalls(onboarding);

        ActionResult<OnboardingDto> result = handler.handle(validSimpleInput(),
            interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo("Error");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("No citizenship info exists for given applicant.");
    }

    @Test
    void invalidCountryCode_ReturnsError() {
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN)));
        mockedServicesCalls(onboarding);
        NonResidentDataDto input = validSimpleInput();
        input.getResidencyAddress().setCountryCode(INVALID_COUNTRY_CODE);

        ActionResult<OnboardingDto> result = handler.handle(input, interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo("Error");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Invalid country code.");
    }


    @Test
    void invalidCitizenshipCountryCode_ReturnsError() {
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN)));
        mockedServicesCalls(onboarding);
        NonResidentDataDto input = validSimpleInput();
        input.setCitizenshipCountryCode("Invalid Value");

        ActionResult<OnboardingDto> result = handler.handle(input, interactionContext);

        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors().get(0).getKey()).isEqualTo("Error");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Invalid country code.");
    }

    @Test
    void uncheckedW8ben_ReturnsError() {
        NonResidentDataDto input = validSimpleInput();
        input.setW8benAccepted(false);

        ActionResult<OnboardingDto> result = handler.handle(input, interactionContext);

        List<Error> errors = result.getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
        assertThat(errors.get(0).getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
        assertThat(errors.get(0).getContext().get("w8benAccepted")).contains("must be true");
    }

    @ParameterizedTest
    @MethodSource("inputWithLackingFields")
    void lackingAnyOfRequiredFields_returnsError(NonResidentDataDto input, String emptyField) {
        ActionResult<OnboardingDto> result = handler.handle(input, interactionContext);

        List<Error> errors = result.getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
        assertThat(errors.get(0).getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
        assertThat(result.getErrors().get(0).getContext()).containsKey(emptyField);
    }

    @Test
    void minimalValidInput_PassesAndSetsFields() {
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN)));
        mockedServicesCalls(onboarding);

        ActionResult<OnboardingDto> result = handler.handle(minimalPassingInput(), interactionContext);
        verify(mapper).write(onboardingCaptor.capture(), any(), any(), eq(caseKey.toString()));
        verify(nonResidentDataMapper).mapNonResidentDataToExistingInfo(any(), any(), any());

        Applicant actual = onboardingCaptor.getValue().getMainApplicant();

        assertThat(result.getErrors().size()).isZero();
    }

    @Test
    void fullValidInput_PassesAndSetsFields() {
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN)));
        mockedServicesCalls(onboarding);

        ActionResult<OnboardingDto> result = handler.handle(validSimpleInput(), interactionContext);
        verify(mapper).write(onboardingCaptor.capture(), any(), any(), eq(caseKey.toString()));
        verify(nonResidentDataMapper).mapNonResidentDataToExistingInfo(any(), any(), any());

        Applicant actual = onboardingCaptor.getValue().getMainApplicant();

        assertThat(result.getErrors().size()).isZero();
    }

    private void mockedServicesCalls(Onboarding onboarding) {
        when(countryService.getCountryListMap())
            .thenReturn(Map.of(VALID_CODE_COUNTRY_1, "Country 3", VALID_CODE_COUNTRY_2, "Country 1",
                VALID_CODE_COUNTRY_3, "Country 2"));
        when(caseDataUtils.getOrCreateCaseKey(interactionContext)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey.toString())).thenReturn(onboarding);
    }

    private static NonResidentDataDto validSimpleInput() {
        return NonResidentDataDto.builder()
            .foreignTin(VALID_FOREIGN_TIN)
            .nationalTin(VALID_NATIONAL_TIN)
            .citizenshipCountryCode(VALID_CODE_COUNTRY_2)
            .residencyAddress(ResidencyAddressDto.builder()
                .city(VALID_CITY)
                .countryCode(VALID_CODE_COUNTRY_3)
                .numberAndStreet(VALID_STREET)
                .zipCode(VALID_ZIP_CODE)
                .build())
            .w8benAccepted(true)
            .withholdingTaxAccepted(true)
            .build();
    }

    private static List<Arguments> inputWithLackingFields() {
        List<Arguments> lackingInput = new ArrayList<>();
        NonResidentDataDto input = minimalPassingInput();
        input.setCitizenshipCountryCode(null);
        lackingInput.add(Arguments.of(input, "citizenshipCountryCode"));

        input = minimalPassingInput();
        input.getResidencyAddress().setCountryCode(null);
        lackingInput.add(Arguments.of(input, "residencyAddress.countryCode"));

        input = minimalPassingInput();
        input.getResidencyAddress().setNumberAndStreet(null);
        lackingInput.add(Arguments.of(input, "residencyAddress.numberAndStreet"));

        input = minimalPassingInput();
        input.getResidencyAddress().setCity(null);
        lackingInput.add(Arguments.of(input, "residencyAddress.city"));

        input = minimalPassingInput();
        input.getResidencyAddress().setZipCode(null);
        lackingInput.add(Arguments.of(input, "residencyAddress.zipCode"));

        input = minimalPassingInput();
        input.setW8benAccepted(null);
        lackingInput.add(Arguments.of(input, "w8benAccepted"));

        input = minimalPassingInput();
        input.setWithholdingTaxAccepted(null);
        lackingInput.add(Arguments.of(input, "withholdingTaxAccepted"));
        return lackingInput;
    }

    private static NonResidentDataDto minimalPassingInput() {
        return NonResidentDataDto.builder()
            .citizenshipCountryCode(VALID_CODE_COUNTRY_2)
            .residencyAddress(ResidencyAddressDto.builder()
                .countryCode(VALID_CODE_COUNTRY_3)
                .numberAndStreet(VALID_STREET)
                .zipCode(VALID_ZIP_CODE)
                .city(VALID_CITY)
                .build())
            .w8benAccepted(true)
            .withholdingTaxAccepted(true)
            .build();
    }
}