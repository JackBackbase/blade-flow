package com.backbase.flow.sme.onboarding.builder;

import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.Applicant.RelationType;
import com.backbase.flow.sme.onboarding.casedata.BusinessDetails;
import com.backbase.flow.sme.onboarding.casedata.BusinessIdentityInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Subtype;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Type;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import com.backbase.flow.sme.onboarding.casedata.Industry;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmeCaseDefBuilder {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String userId;
    private String phoneNumber;
    private Boolean isRegistrar;
    private RelationType relationType;
    private Double ownershipPercentage;
    private String role;
    private String businessIndustry;
    private String businessKnownName;
    private String businessLegalName;
    private String businessOperationState;
    private List<DocumentRequest> documentRequests = new ArrayList<>();
    private Address personalAddress;
    private Address businessAddress;
    private AmlInfo businessAmlInfo;
    private AmlInfo personalAmlInfo;
    private Type businessType;
    private Subtype businessSubtype;
    private BusinessRelationsCaseData businessRelations;
    private BusinessRelationsCaseData.Status status;
    private BusinessRelationsCaseData.BusinessRelationType businessRelationType;

    public static SmeCaseDefBuilder getInstance() {
        return new SmeCaseDefBuilder();
    }

    public SmeCaseDefBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SmeCaseDefBuilder isRegistrar(Boolean isRegistrar) {
        this.isRegistrar = isRegistrar;
        return this;
    }

    public SmeCaseDefBuilder relationType(RelationType relationType) {
        this.relationType = relationType;
        return this;
    }

    public SmeCaseDefBuilder ownershipPercentage(Double ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
        return this;
    }

    public SmeCaseDefBuilder role(String role) {
        this.role = role;
        return this;
    }

    public SmeCaseDefBuilder status(BusinessRelationsCaseData.Status status) {
        this.status = status;
        return this;
    }

    public SmeCaseDefBuilder businessRelationType(BusinessRelationsCaseData.BusinessRelationType businessRelationType) {
        this.businessRelationType = businessRelationType;
        return this;
    }

    public SmeCaseDefBuilder businessRelations(BusinessRelationsCaseData businessRelations) {
        this.businessRelations = businessRelations;
        return this;
    }

    public SmeCaseDefBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SmeCaseDefBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public SmeCaseDefBuilder dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public SmeCaseDefBuilder email(String email) {
        this.email = email;
        return this;
    }

    public SmeCaseDefBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public SmeCaseDefBuilder soleProp() {
        this.businessType = Type.SOLE_PROPRIETORSHIP;
        return this;
    }

    public SmeCaseDefBuilder partnership() {
        this.businessType = Type.PARTNERSHIP;
        return this;
    }

    public SmeCaseDefBuilder corporation() {
        this.businessType = Type.CORPORATION;
        return this;
    }

    public SmeCaseDefBuilder generalSubtype() {
        this.businessSubtype = Subtype.GENERAL;
        return this;
    }

    public SmeCaseDefBuilder jointVentureSubtype() {
        this.businessSubtype = Subtype.JOINT_VENTURE;
        return this;
    }

    public SmeCaseDefBuilder businessIndustry(String businessIndustry) {
        this.businessIndustry = businessIndustry;
        return this;
    }

    public SmeCaseDefBuilder businessKnownName(String businessKnownName) {
        this.businessKnownName = businessKnownName;
        return this;
    }

    public SmeCaseDefBuilder businessLegalName(String businessLegalName) {
        this.businessLegalName = businessLegalName;
        return this;
    }

    public SmeCaseDefBuilder businessOperationState(String businessOperationState) {
        this.businessOperationState = businessOperationState;
        return this;
    }

    public SmeCaseDefBuilder documentRequests(List<DocumentRequest> documentRequests) {
        this.documentRequests = documentRequests;
        return this;
    }

    public SmeCaseDefBuilder personalAddress(Address personalAddress) {
        this.personalAddress = personalAddress;
        return this;
    }

    public SmeCaseDefBuilder businessAddress(Address businessAddress) {
        this.businessAddress = businessAddress;
        return this;
    }

    public SmeCaseDefBuilder businessAmlInfo(AmlInfo businessAmlInfo) {
        this.businessAmlInfo = businessAmlInfo;
        return this;
    }

    public SmeCaseDefBuilder personalAmlInfo(AmlInfo personalAmlInfo) {
        this.personalAmlInfo = personalAmlInfo;
        return this;
    }

    public SmeCaseDefinition build() {
        SmeCaseDefinition smeCase = new SmeCaseDefinition();
        smeCase.setCompanyLookupInfo(new CompanyLookupInfo());
        smeCase.getCompanyLookupInfo().setBusinessStructureInfo(new BusinessStructureInfo());
        smeCase.getCompanyLookupInfo().getBusinessStructureInfo().setType(businessType);
        smeCase.getCompanyLookupInfo().getBusinessStructureInfo().setSubtype(businessSubtype);
        smeCase.getCompanyLookupInfo().setBusinessAddressInfo(businessAddress);
        smeCase.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().setDocumentRequests(documentRequests);

        if (firstName != null || lastName != null || dateOfBirth != null || phoneNumber != null
            || email != null || userId != null || isRegistrar != null || Objects.nonNull(personalAddress)
            || relationType != null || ownershipPercentage != null || role != null || Objects
            .nonNull(personalAmlInfo)) {
            Applicant applicant = new Applicant();
            applicant.setFirstName(firstName);
            applicant.setLastName(lastName);
            applicant.setDateOfBirth(dateOfBirth);
            applicant.setEmail(email);
            applicant.setPhoneNumber(phoneNumber);
            applicant.setIsRegistrar(isRegistrar);
            applicant.setRelationType(relationType);
            applicant.setOwnershipPercentage(ownershipPercentage);
            applicant.setRole(role);
            applicant.setPersonalAddress(personalAddress);
            applicant.setId(userId);
            applicant.setAntiMoneyLaunderingInfo(personalAmlInfo);
            smeCase.setApplicants(Collections.singletonList(applicant));
        }

        if (businessIndustry != null) {
            Industry industry = new Industry();
            List<Industry> industryList = new ArrayList<>();
            industry.setCode(businessIndustry);
            industryList.add(industry);
            smeCase.getCompanyLookupInfo().setBusinessIdentityInfo(new BusinessIdentityInfo());
            smeCase.getCompanyLookupInfo().getBusinessIdentityInfo().setIndustries(industryList);
        }
        if (businessRelations != null) {
            smeCase.setBusinessRelations(businessRelations);
        }
        smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().setLegalName(businessLegalName);
        smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().setDba(businessKnownName);
        smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().setStateOperatingIn(businessOperationState);
        smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().setAntiMoneyLaunderingInfo(businessAmlInfo);

        return smeCase;
    }
}
