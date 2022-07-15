package com.backbase.flow.sme.onboarding.mapper;

import com.backbase.flow.businessrelations.casedata.BusinessPerson;
import com.backbase.flow.businessrelations.casedata.BusinessRelationsCaseData;
import com.backbase.flow.service.businessrelations.platform.BusinessRelationsCurrentUserData;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessRelationsMapper {

    BusinessRelationsMapper INSTANCE = Mappers.getMapper(BusinessRelationsMapper.class);

    BusinessRelationsCurrentUserData toBusinessRelationsCurrentUserData(Applicant applicant);

    @Mapping(target = "processInstanceId", source = "businessRelations.processInstanceId")
    @Mapping(target = "businessRelationType", source = "businessRelations.businessRelationType")
    @Mapping(target = "status", source = "businessRelations.status")
    @Mapping(target = "reviewInformation", source = "businessRelations.reviewInformation")
    @Mapping(target = "businessPersons", source = "applicants")
    BusinessRelationsCaseData toBusinessRelationsCaseData(SmeCaseDefinition smeCase);

    @Mapping(target = "phone", source = "phoneNumber")
    @Mapping(target = "currentUser", source = "isRegistrar")
    BusinessPerson toBusinessPerson(Applicant applicant);

    com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData toSmeBusinessRelationsCaseData(
        BusinessRelationsCaseData businessRelationsCaseData
    );

    @InheritInverseConfiguration
    void updateApplicant(@MappingTarget Applicant applicant, BusinessPerson businessPerson);
}
