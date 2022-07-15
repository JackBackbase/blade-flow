package com.backbase.flow.onboarding.us.util;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.CredentialsResponseDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCitizenshipDataResponseDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCoApplicantDataResponseDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.FetchCustomerDataResponseDto;
import com.backbase.flow.onboarding.us.process.dto.ReviewClientRejectedDto;
import java.beans.FeatureDescriptor;
import java.util.stream.Stream;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

@Mapper
public abstract class OnboardingMapper {

    public static final OnboardingMapper MAPPER = Mappers.getMapper(OnboardingMapper.class);

    @Mapper
    interface Internal {
        Internal MAPPER = Mappers.getMapper(Internal.class);

        @Mapping(source = "applicant.citizenship.citizenshipType", target = "citizenshipType")
        @Mapping(source = "applicant.citizenship.ssn", target = "ssn")
        @Mapping(source = "applicant.citizenship.citizenshipReview", target = "citizenshipReview")
        @Mapping(ignore = true, target = "isJointAccount")
        @Mapping(ignore = true, target = "isMainApplicantFlow")
        OnboardingDto mapToDto(Applicant applicant);
    }

    public abstract OnboardingDto mapToDto(Onboarding onboarding);

    @AfterMapping
    protected OnboardingDto afterMapToDto(Onboarding onboarding, @MappingTarget OnboardingDto targetOnboarding) {
        var applicant = OnboardingCaseDataUtils.getApplicant(onboarding);
        // map applicant with internal mapper
        var targetWithApplicant = Internal.MAPPER.mapToDto(applicant);
        // merge mapping results
        copyNotNullProperties(targetOnboarding, targetWithApplicant);
        return targetWithApplicant;
    }

    @Mapping(source = "kycInformation.answers", target = "kycInformation")
    @Mapping(ignore = true, target = "rejectionCode")
    public abstract ReviewClientRejectedDto mapForUserTask(Applicant applicant);

    public abstract FetchCustomerDataResponseDto mapToLoginResponseDto(Applicant applicant, Boolean isMainApplicant);

    public abstract CredentialsResponseDto mapToCredentialsResponseDto(Applicant applicant, Boolean isJointAccount,
        Boolean isMainApplicantFlow);

    public abstract FetchCoApplicantDataResponseDto mapToFetchCoApplicantDataResponseDto(Onboarding onboarding);

    @Mapping(source = "citizenship.citizenshipType", target = "type")
    @Mapping(source = "citizenship.ssn", target = "ssn")
    @Mapping(source = "citizenship", target = "nonResident")
    @Mapping(source = "citizenship.nonResident.citizenshipCountryCode", target = "nonResident.citizenshipCountryCode")
    @Mapping(source = "citizenship.nonResident.residencyAddress", target = "nonResident.residencyAddress")
    @Mapping(source = "citizenship.w8ben.accepted", target = "nonResident.w8benAccepted")
    public abstract FetchCitizenshipDataResponseDto mapToCitizenshipDto(Applicant applicant);

    @AfterMapping
    protected void afterMapToCitizenshipDto(Applicant source, @MappingTarget FetchCitizenshipDataResponseDto target) {
        CitizenshipInfo citizenship = source.getCitizenship();

        // Preventing creation of empty nonResident object
        if (citizenship == null || citizenship.getNonResident() == null) {
            target.setNonResident(null);
            return;
        }

        // Workaround for generating sources through intellij
        target.getNonResident().setWithholdingTaxAccepted(citizenship.getWithholdingTaxAccepted());
    }

    private void copyNotNullProperties(final Object source, Object target) {
        final var wrappedSource = new BeanWrapperImpl(source);
        final var nullPropertyNames = Stream.of(wrappedSource.getPropertyDescriptors())
            .map(FeatureDescriptor::getName)
            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
            .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, nullPropertyNames);
    }

}
