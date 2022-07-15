package com.backbase.flow.onboarding.us.mapper.citizenship;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.service.CountryService;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CountryCodeToCountryDecorator implements NonResidentDataMapper {

    @Autowired
    private NonResidentDataMapper delegate;
    @Autowired
    private CountryService countryService;

    @Override
    public void mapNonResidentDataToExistingInfo(CitizenshipInfo target, NonResidentDataDto nonResidentData,
        OffsetDateTime policyAcceptanceTime) {
        delegate.mapNonResidentDataToExistingInfo(target, nonResidentData, policyAcceptanceTime);
        target.getNonResident()
            .getResidencyAddress()
            .setCountry(countryService.getCountryListMap().get(nonResidentData.getResidencyAddress().getCountryCode()));
        target.getNonResident()
            .setCitizenshipCountry(countryService.getCountryListMap().get(nonResidentData.getCitizenshipCountryCode()));
    }
}
