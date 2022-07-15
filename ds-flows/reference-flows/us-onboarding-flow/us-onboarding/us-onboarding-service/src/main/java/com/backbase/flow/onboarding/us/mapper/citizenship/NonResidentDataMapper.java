package com.backbase.flow.onboarding.us.mapper.citizenship;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.process.dto.CitizenshipUserTaskInfoDto;
import java.time.OffsetDateTime;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(CountryCodeToCountryDecorator.class)
public interface NonResidentDataMapper {

    @Mapping(target = "nonResident", source = "nonResidentData")
    @Mapping(target = ".", source = "nonResidentData")
    @Mapping(target = "w8ben.accepted", source = "nonResidentData.w8benAccepted")
    @Mapping(target = "w8ben.acceptedAt", source = "policyAcceptanceTime")
    void mapNonResidentDataToExistingInfo(@MappingTarget CitizenshipInfo target, NonResidentDataDto nonResidentData,
        OffsetDateTime policyAcceptanceTime);

    @Mapping(target = "fullName", expression = "java(applicant.getFirstName()+ \" \" + applicant.getLastName())")
    @Mapping(target = "dateOfBirth", source = "applicant.dateOfBirth")
    @Mapping(target = "citizenship", source = "applicant.citizenship")
    CitizenshipUserTaskInfoDto fromApplicant(Applicant applicant);

}
