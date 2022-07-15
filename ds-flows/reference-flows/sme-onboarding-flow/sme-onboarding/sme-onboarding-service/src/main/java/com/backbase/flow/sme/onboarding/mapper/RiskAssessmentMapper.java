package com.backbase.flow.sme.onboarding.mapper;

import com.backbase.flow.riskassessment.casedata.BusinessInfo;
import com.backbase.flow.riskassessment.casedata.Person;
import com.backbase.flow.riskassessment.casedata.RiskAssessmentCaseData;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.Industry;
import com.backbase.flow.sme.onboarding.casedata.RiskAssessmentInfo;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiskAssessmentMapper extends ApplicantMapper {

    RiskAssessmentMapper INSTANCE = Mappers.getMapper(RiskAssessmentMapper.class);

    com.backbase.flow.sme.onboarding.casedata.RiskAssessmentCaseData toSmeRiskAssessmentCaseData(
        RiskAssessmentCaseData riskAssessmentCaseData
    );

    RiskAssessmentInfo toRiskAssessmentInfo(Person person);

    @Mapping(target = ".", source = "riskAssessment")
    @Mapping(target = "peopleInfo.people", source = "applicants")
    @Mapping(target = "peopleInfo.peopleRiskScore", source = "riskAssessment.peopleInfo.peopleRiskScore")
    @Mapping(target = "peopleInfo.peopleRiskLevel", source = "riskAssessment.peopleInfo.peopleRiskLevel")
    RiskAssessmentCaseData toRiskAssessmentCaseData(SmeCaseDefinition smeCaseDefinition);

    @Mapping(target = ".", source = "riskAssessment.businessInfo")
    @Mapping(target = "businessLegalName", source = "companyLookupInfo.businessDetailsInfo.legalName")
    @Mapping(target = "amlKybMatchStatus", source = "companyLookupInfo.businessDetailsInfo.antiMoneyLaunderingInfo.amlResult.matchStatus")
    @Mapping(target = "reviewAmlKybTaskOutcome", source = "companyLookupInfo.businessDetailsInfo.antiMoneyLaunderingInfo.reviewApproved")
    @Mapping(target = "businessIndustries", source = "companyLookupInfo.businessIdentityInfo.industries")
    BusinessInfo toBusinessInfo(SmeCaseDefinition smeCaseDefinition);

    default String toRiskAssessmentIndustry(Industry industry) {
        return industry.getDescription();
    }

    @Mapping(target = ".", source = "riskAssessmentInfo")
    @Mapping(target = "name", expression = "java(fullName(applicant))")
    @Mapping(target = "amlKycMatchStatus", source = "antiMoneyLaunderingInfo.amlResult.matchStatus")
    @Mapping(target = "reviewAmlKycTaskOutcome", source = "antiMoneyLaunderingInfo.reviewApproved")
    Person toPerson(Applicant applicant);
}
