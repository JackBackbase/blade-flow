package com.backbase.flow.onboarding.us.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.application.uso.casedata.AmlResult;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.application.uso.casedata.IdentityVerificationResult;
import com.backbase.flow.application.uso.casedata.NonResidentInfo;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.application.uso.casedata.ResidencyAddress;
import com.backbase.flow.application.uso.casedata.W8ben;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.ResidencyAddressDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCitizenshipDataResponseDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCustomerDataResponseDto;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class OnboardingMapperTest {

    private final OnboardingMapper mapper = OnboardingMapper.MAPPER;

    @Test
    void mapToDto_allFieldsAreSet() {
        var onboarding = sampleOnboarding();

        var onboardingDto = mapper.mapToDto(onboarding);

        assertOnboarding(onboardingDto, onboarding);
    }

    private void assertOnboarding(OnboardingDto onboardingDto, Onboarding onboarding) {
        assertThat(onboardingDto.getIsMainApplicantFlow()).isEqualTo(onboarding.getIsMainApplicantFlow());
        assertThat(onboardingDto.getIsJointAccount()).isEqualTo(onboarding.getIsJointAccount());
        assertApplicant(onboardingDto, onboarding.getMainApplicant());
    }

    private void assertApplicant(OnboardingDto onboardingDto, Applicant applicant) {
        assertThat(onboardingDto.getFirstName()).isEqualTo(applicant.getFirstName());
        assertThat(onboardingDto.getLastName()).isEqualTo(applicant.getLastName());
        assertThat(onboardingDto.getDateOfBirth()).isEqualTo(applicant.getDateOfBirth());
        assertThat(onboardingDto.getPhoneNumber()).isEqualTo(applicant.getPhoneNumber());
        assertThat(onboardingDto.getEmail()).isEqualTo(applicant.getEmail());
        assertThat(onboardingDto.getAddress()).isEqualTo(applicant.getAddress());
        assertThat(onboardingDto.getAntiMoneyLaunderingInfo()).isEqualTo(applicant.getAntiMoneyLaunderingInfo());
        assertThat(onboardingDto.getIdentityVerificationResult()).isEqualTo(applicant.getIdentityVerificationResult());
        assertThat(onboardingDto.getCitizenshipType()).isEqualTo(applicant.getCitizenship().getCitizenshipType());
        assertThat(onboardingDto.getCitizenshipReview()).isEqualTo(applicant.getCitizenship().getCitizenshipReview());
        assertThat(onboardingDto.getSsn()).isEqualTo(applicant.getCitizenship().getSsn());
    }

    private Onboarding sampleOnboarding() {
        return new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(sampleApplicant())
            .withIsJointAccount(false);
    }

    private Applicant sampleApplicant() {
        return new Applicant()
            .withFirstName("First Name")
            .withLastName("Last Name")
            .withDateOfBirth("DOB")
            .withPhoneNumber("Phone Number")
            .withEmail("Email")
            .withAddress(sampleAddress())
            .withAntiMoneyLaunderingInfo(sampleAntiMoneyLaunderingInfo())
            .withIdentityVerificationResult(sampleIdentityVerificationResult())
            .withCitizenship(sampleCitizenship());
    }

    private Address sampleAddress() {
        return new Address()
            .withNumberAndStreet("Number and Street")
            .withZipCode("Zip Code")
            .withCity("City")
            .withState("State")
            .withApt("Apartment");
    }

    private OnboardingAntiMoneyLaunderingInfo sampleAntiMoneyLaunderingInfo() {
        return new OnboardingAntiMoneyLaunderingInfo()
            .withStatus(Status.SUCCESS)
            .withAmlResult(new AmlResult()
                .withMatchStatus("no_match"));
    }

    private IdentityVerificationResult sampleIdentityVerificationResult() {
        return new IdentityVerificationResult()
            .withDocumentStatus("APPROVED_VERIFIED");
    }

    private CitizenshipInfo sampleCitizenship() {
        return new CitizenshipInfo()
            .withCitizenshipType(CitizenshipType.US_CITIZEN)
            .withCitizenshipReview(new CitizenshipReview()
                .withNeeded(true)
                .withApproved(true)
                .withComment("Review comment"))
            .withSsn("123456789");
    }

    @Test
    void mapToLoginResponseDto_addressExists_addressIsMapped() {
        Applicant applicant = new Applicant().withAddress(sampleAddress());

        FetchCustomerDataResponseDto.Address expectedAddress = new FetchCustomerDataResponseDto.Address();
        expectedAddress.setNumberAndStreet("Number and Street");
        expectedAddress.setZipCode("Zip Code");
        expectedAddress.setCity("City");
        expectedAddress.setState("State");
        expectedAddress.setApt("Apartment");
        FetchCustomerDataResponseDto expectedResult = new FetchCustomerDataResponseDto();
        expectedResult.setAddress(expectedAddress);
        expectedResult.setIsMainApplicant(true);

        FetchCustomerDataResponseDto actualResult = mapper.mapToLoginResponseDto(applicant, true);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void mapToCitizenshipDtoApplicantWithEmptyCitizenship() {
        FetchCitizenshipDataResponseDto fetchCitizenshipDataResponseDto = mapper.mapToCitizenshipDto(new Applicant());

        assertThat(fetchCitizenshipDataResponseDto.getType()).isNull();
        assertThat(fetchCitizenshipDataResponseDto.getSsn()).isNull();
        assertThat(fetchCitizenshipDataResponseDto.getNonResident()).isNull();
    }

    @Test
    void mapToCitizenshipDtoUsCitizen() {
        FetchCitizenshipDataResponseDto fetchCitizenshipDataResponseDto = mapper.mapToCitizenshipDto(
            new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(CitizenshipType.US_CITIZEN)
                    .withSsn("123456789")));

        assertThat(fetchCitizenshipDataResponseDto.getType()).isEqualTo(CitizenshipType.US_CITIZEN);
        assertThat(fetchCitizenshipDataResponseDto.getSsn()).isEqualTo("123456789");
        assertThat(fetchCitizenshipDataResponseDto.getNonResident()).isNull();
    }

    @Test
    void mapToCitizenshipDtoNonResidentAlien() {
        Applicant toMap = new Applicant()
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

        FetchCitizenshipDataResponseDto fetchCitizenshipDataResponseDto = mapper.mapToCitizenshipDto(toMap);

        assertThat(fetchCitizenshipDataResponseDto.getType()).isEqualTo(CitizenshipType.NON_RESIDENT_ALIEN);
        assertThat(fetchCitizenshipDataResponseDto.getSsn()).isNull();

        NonResidentDataDto nonResident = fetchCitizenshipDataResponseDto.getNonResident();
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
    void mapToCitizenshipDtoEmptyResult() {
        FetchCitizenshipDataResponseDto fetchCitizenshipDataResponseDto = mapper.mapToCitizenshipDto(null);
        assertThat(fetchCitizenshipDataResponseDto).isNull();
    }

}
