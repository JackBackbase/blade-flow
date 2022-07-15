package com.backbase.flow.onboarding.us.mapper.citizenship;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.NonResidentInfo;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.ResidencyAddressDto;
import com.backbase.flow.onboarding.us.process.dto.CitizenshipUserTaskInfoDto;
import com.backbase.flow.onboarding.us.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NonResidentDataMapperImpl.class, NonResidentDataMapperImpl_.class, CountryService.class,
    ObjectMapper.class})
class NonResidentDataMapperTest {

    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String DATE_OF_BIRTH = "01-01-2000";
    @Autowired
    private NonResidentDataMapperImpl nonResidentDataMapper;

    @Test
    void mapNonResidentDataToExistingInfo() {
        CitizenshipInfo citizenshipInfo = new CitizenshipInfo();
        NonResidentDataDto nonResidentData = NonResidentDataDto.builder()
            .foreignTin("123456789")
            .nationalTin("987654321")
            .withholdingTaxAccepted(true)
            .w8benAccepted(true)
            .citizenshipCountryCode("CT1")
            .residencyAddress(ResidencyAddressDto.builder()
                .city("Moscow")
                .countryCode("CT1")
                .zipCode("119600")
                .numberAndStreet("11")
                .build())
            .build();
        OffsetDateTime policyAcceptanceTime = OffsetDateTime.now();

        nonResidentDataMapper.mapNonResidentDataToExistingInfo(citizenshipInfo, nonResidentData, policyAcceptanceTime);

        assertThat(citizenshipInfo.getForeignTin()).isEqualTo(nonResidentData.getForeignTin());
        assertThat(citizenshipInfo.getNationalTin()).isEqualTo(nonResidentData.getNationalTin());
        assertThat(citizenshipInfo.getWithholdingTaxAccepted()).isEqualTo(nonResidentData.getWithholdingTaxAccepted());
        assertThat(citizenshipInfo.getW8ben().getAccepted()).isEqualTo(nonResidentData.getW8benAccepted());
        assertThat(citizenshipInfo.getW8ben().getAcceptedAt()).isEqualTo(policyAcceptanceTime);
        assertThat(citizenshipInfo.getNonResident().getCitizenshipCountryCode())
            .isEqualTo(nonResidentData.getCitizenshipCountryCode());
        assertThat(citizenshipInfo.getNonResident().getCitizenshipCountry()).isEqualTo("Country 1");

        assertThat(citizenshipInfo.getNonResident().getResidencyAddress().getCity())
            .isEqualTo(nonResidentData.getResidencyAddress().getCity());
        assertThat(citizenshipInfo.getNonResident().getResidencyAddress().getCountryCode())
            .isEqualTo(nonResidentData.getResidencyAddress().getCountryCode());
        assertThat(citizenshipInfo.getNonResident().getResidencyAddress().getCountry()).isEqualTo("Country 1");
        assertThat(citizenshipInfo.getNonResident().getResidencyAddress().getZipCode())
            .isEqualTo(nonResidentData.getResidencyAddress().getZipCode());
        assertThat(citizenshipInfo.getNonResident().getResidencyAddress().getNumberAndStreet())
            .isEqualTo(nonResidentData.getResidencyAddress().getNumberAndStreet());

    }

    @Test
    void fromApplicant() {
        NonResidentInfo nonResidentInfo = new NonResidentInfo();
        nonResidentInfo.setCitizenshipCountry("Country");

        CitizenshipInfo citizenshipInfo = new CitizenshipInfo();
        citizenshipInfo.setForeignTin("123456789");
        citizenshipInfo.setCitizenshipType(CitizenshipType.NON_RESIDENT_ALIEN);
        citizenshipInfo.setNonResident(nonResidentInfo);

        Applicant applicant = new Applicant();
        applicant.setFirstName(FIRST_NAME);
        applicant.setLastName(LAST_NAME);
        applicant.setDateOfBirth(DATE_OF_BIRTH);
        applicant.setCitizenship(citizenshipInfo);

        CitizenshipUserTaskInfoDto citizenshipUserTaskInfoDto = nonResidentDataMapper.fromApplicant(applicant);

        assertThat(citizenshipUserTaskInfoDto.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
        assertThat(citizenshipUserTaskInfoDto.getFullName()).isEqualTo(FIRST_NAME + " " + LAST_NAME);
        assertThat(citizenshipUserTaskInfoDto.getCitizenship()).isEqualTo(citizenshipInfo);
    }
}