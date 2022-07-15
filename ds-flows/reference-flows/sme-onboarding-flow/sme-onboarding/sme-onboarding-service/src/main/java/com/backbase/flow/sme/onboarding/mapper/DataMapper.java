package com.backbase.flow.sme.onboarding.mapper;

import com.backbase.flow.casedata.api.model.FlowMilestoneDto;
import com.backbase.flow.casedata.tracking.CaseMilestone;
import com.backbase.flow.integration.service.data.DocumentRequestData;
import com.backbase.flow.service.aml.casedata.AmlBusinessApplicant;
import com.backbase.flow.service.aml.casedata.AmlPersonApplicant;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.BusinessDetails;
import com.backbase.flow.sme.onboarding.casedata.TermsAndConditions;
import com.backbase.flow.sme.onboarding.event.TermsAndConditionsApprovedEvent;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.ApplicantDocumentRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.TermsConditionsRequestDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DataMapper extends ApplicantMapper {

    @Mapping(target = "email", source = "emailAddress")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    Applicant toRegistrar(AnchorRequestDto request);

    @Mapping(target = "acceptanceDate", expression = "java(java.time.OffsetDateTime.now())")
    TermsAndConditions toTermsAndConditions(TermsConditionsRequestDto payload);

    default TermsAndConditionsApprovedEvent toTermsAndConditionsEvent(UUID caseKey) {
        return new TermsAndConditionsApprovedEvent(caseKey);
    }

    @Mapping(target = "customer", source = "applicant")
    @Mapping(target = "customer.userId", source = "applicant.id")
    @Mapping(target = "customer.emailAddress", source = "applicant.email")
    DocumentRequestData toDocumentRequestData(ApplicantDocumentRequestDto requestDto);

    com.backbase.flow.sme.onboarding.casedata.DocumentRequest.Status toStatus(
        com.backbase.flow.integration.service.data.DocumentRequest.Status status);

    @Mapping(target = "amlBusinessApplicant.businessName", expression = "java(businessNameForAml(businessDetails))")
    @Mapping(target = "reviewApproved", source = "antiMoneyLaunderingInfo.reviewApproved")
    @Mapping(target = "reviewNeeded", source = "antiMoneyLaunderingInfo.reviewNeeded")
    @Mapping(target = "reviewApprovedReason", source = "antiMoneyLaunderingInfo.reviewApprovedReason")
    @Mapping(target = "reviewDeclinedComment", source = "antiMoneyLaunderingInfo.reviewDeclinedComment")
    @Mapping(target = "status", source = "antiMoneyLaunderingInfo.status")
    @Mapping(target = "amlResult", source = "antiMoneyLaunderingInfo.amlResult")
    AntiMoneyLaunderingInfo toAntiMoneyLaunderingInfo(BusinessDetails businessDetails);

   default String businessNameForAml(BusinessDetails businessDetails) {
        if(Boolean.TRUE.equals(businessDetails.getUseDbaForAml())) {
            return businessDetails.getDba();
        }
        return businessDetails.getLegalName();
    }

    @Mapping(target = "amlPersonApplicant.fullName", expression = "java(fullName(applicant))")
    @Mapping(target = "amlPersonApplicant.yearOfBirth", expression = "java(yob(applicant))")
    @Mapping(target = "reviewApproved", source = "antiMoneyLaunderingInfo.reviewApproved")
    @Mapping(target = "reviewNeeded", source = "antiMoneyLaunderingInfo.reviewNeeded")
    @Mapping(target = "reviewApprovedReason", source = "antiMoneyLaunderingInfo.reviewApprovedReason")
    @Mapping(target = "reviewDeclinedComment", source = "antiMoneyLaunderingInfo.reviewDeclinedComment")
    @Mapping(target = "status", source = "antiMoneyLaunderingInfo.status")
    @Mapping(target = "amlResult", source = "antiMoneyLaunderingInfo.amlResult")
    AntiMoneyLaunderingInfo toAntiMoneyLaunderingInfo(Applicant applicant);

    AmlInfo toAmlInfo(AntiMoneyLaunderingInfo antiMoneyLaunderingInfo);

    @Mapping(target = "businessName", expression = "java(businessNameForAml(businessDetails))")
    AmlBusinessApplicant toAmlBusinessApplicant(BusinessDetails businessDetails);

    @Mapping(target = "epic", source = "caseMilestoneId.epic")
    @Mapping(target = "name", source = "caseMilestoneId.name")
    FlowMilestoneDto toFlowMilestoneDto(CaseMilestone caseMilestone);

    @Mapping(target = "fullName", expression = "java(fullName(applicant))")
    @Mapping(target = "yearOfBirth", expression = "java(yob(applicant))")
    AmlPersonApplicant toAmlPersonApplicant(Applicant applicant);
}
