package com.backbase.flow.sme.onboarding.process.helper;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static com.backbase.flow.service.process.ProcessVariables.IDV_RESULT;
import static com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants.APPLICANT_ID;
import static com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants.APPLICATION_SUBMITTED_EVENT;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.events.PersistedEventFilter;
import com.backbase.flow.casedata.events.PersistedEventRepository;
import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.process.annotation.ProcessBean;
import com.backbase.flow.service.bpmnprocess.CaseKey;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.BusinessDetails;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Type;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest.Status;
import com.backbase.flow.sme.onboarding.casedata.RiskAssessmentCaseData;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.casedata.TermsAndConditions;
import com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants;
import com.backbase.flow.sme.onboarding.event.ApplicantActionNotRequiredEvent;
import com.backbase.flow.sme.onboarding.event.ApplicantOnboardingFinishedEvent;
import com.backbase.flow.sme.onboarding.event.ApplicantOnboardingPendingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.context.VariableContext;
import org.springframework.stereotype.Component;

@ProcessBean
@Component("smeHelper")
@RequiredArgsConstructor
public class SmeProcessHelper {

    private final ObjectMapper objectMapper;
    private final CaseDataService caseDataService;
    private final PersistedEventRepository persistedEventRepository;
    private final CaseEngineEventPublisher publisher;

    @RunWithoutAuthorization
    public boolean isDocumentsApproved(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);

        return smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getDocumentRequests().stream()
            .allMatch(d -> Status.APPROVED.equals(d.getStatus()) || Status.CANCELLED.equals(d.getStatus()));
    }

    @RunWithoutAuthorization
    public boolean isApplicationSubmitted(DelegateExecution execution) {
        return persistedEventRepository.filterEvents(CaseKey.from(execution).getKey(),
                new PersistedEventFilter()).stream()
            .anyMatch(event -> APPLICATION_SUBMITTED_EVENT.equals(event.getEventName()));
    }

    @RunWithoutAuthorization
    public boolean readyForDocumentGathering(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        boolean businessStructureCheck = smeCase != null
            && smeCase.getCompanyLookupInfo() != null
            && smeCase.getCompanyLookupInfo().getBusinessStructureInfo() != null
            && smeCase.getCompanyLookupInfo().getBusinessStructureInfo().getType() != null
            && (smeCase.getCompanyLookupInfo().getBusinessStructureInfo().getSubtype() != null
            || smeCase.getCompanyLookupInfo().getBusinessStructureInfo().getType()
            == Type.SOLE_PROPRIETORSHIP);
        return businessStructureCheck
            && smeCase.getCompanyLookupInfo().getBusinessIdentityInfo() != null
            && smeCase.getCompanyLookupInfo().getBusinessIdentityInfo().getIndustries() != null
            && StringUtils.isNotEmpty(smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getStateOperatingIn());
    }

    @RunWithoutAuthorization
    public void saveUseDbaForAml(DelegateExecution execution, Boolean useDbaForAml) {
        updateCase(execution, smeCase -> smeCase.getCompanyLookupInfo()
            .getBusinessDetailsInfo()
            .setUseDbaForAml(useDbaForAml));
    }

    @RunWithoutAuthorization
    public boolean amlKybSucceeded(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);

        return AmlInfo.Status.SUCCESS
            .equals(smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo().getStatus());
    }

    @RunWithoutAuthorization
    public boolean amlKybFailed(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);

        return AmlInfo.Status.FAILED
            .equals(smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo().getStatus());
    }

    @RunWithoutAuthorization
    public boolean businessRelationKybSucceeded(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        return BusinessRelationsCaseData.Status.COMPLETE.equals(smeCase.getBusinessRelations().getStatus());
    }

    @RunWithoutAuthorization
    public boolean businessRelationKybFailed(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        return BusinessRelationsCaseData.Status.INCOMPLETE.equals(smeCase.getBusinessRelations().getStatus());
    }

    @RunWithoutAuthorization
    public boolean amlKycSucceeded(DelegateExecution execution, String userId) {
        var smeCase = getSmeCase(execution);

        return smeCase.getApplicants().stream().filter(applicant -> userId.equals(applicant.getId()))
            .findFirst().orElseThrow().getAntiMoneyLaunderingInfo().getStatus().equals(AmlInfo.Status.SUCCESS);
    }

    @RunWithoutAuthorization
    public boolean amlKycFailed(DelegateExecution execution, String applicantId) {
        var smeCase = getSmeCase(execution);

        return smeCase.getApplicants().stream().filter(applicant -> applicantId.equals(applicant.getId()))
            .findFirst().orElseThrow().getAntiMoneyLaunderingInfo().getStatus().equals(AmlInfo.Status.FAILED);
    }

    @RunWithoutAuthorization
    public boolean idvSucceeded(DelegateExecution execution) {
        return "approved".equals(execution.getVariable(IDV_RESULT));
    }

    @RunWithoutAuthorization
    public boolean idvFailed(DelegateExecution execution) {
        return "denied".equals(execution.getVariable(IDV_RESULT));
    }

    @RunWithoutAuthorization
    public boolean termsAccepted(DelegateExecution execution) {
        return Optional.ofNullable(getSmeCase(execution).getTermsAndConditions())
            .map(TermsAndConditions::getAccepted)
            .orElse(false);
    }

    @RunWithoutAuthorization
    public String getBusinessType(VariableContext variableContext) {
        return getCompanyLookupInfo(variableContext)
            .map(CompanyLookupInfo::getBusinessStructureInfo)
            .map(BusinessStructureInfo::getType)
            .map(BusinessStructureInfo.Type::toString)
            .orElse(null);
    }

    @RunWithoutAuthorization
    @SuppressWarnings("rawtypes")
    public List<Map> getAdditionalApplicants(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);

        return smeCase.getApplicants().stream().filter(applicant -> !applicant.getIsRegistrar())
            .map(d -> objectMapper.convertValue(d, Map.class)).collect(Collectors.toList());
    }

    @RunWithoutAuthorization
    public String getRegistrarId(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);

        return smeCase.getApplicants().stream()
            .filter(Applicant::getIsRegistrar)
            .findFirst()
            .orElseThrow()
            .getId();
    }

    @RunWithoutAuthorization
    public boolean isAdditionalApplicant(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        return smeCase.getApplicants().stream()
            .anyMatch(applicant -> Boolean.FALSE.equals(applicant.getIsRegistrar()));
    }

    @RunWithoutAuthorization
    public boolean isBusinessRelationComplete(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        var isBusinessRelationRequired = smeCase.getBusinessRelationRequired();
        if (isBusinessRelationRequired == null) {
            return false;
        }
        if (!isBusinessRelationRequired) {
            return true;
        }
        return Optional.ofNullable(smeCase.getBusinessRelations())
            .map(BusinessRelationsCaseData::getStatus)
            .filter(BusinessRelationsCaseData.Status.COMPLETE::equals)
            .isPresent();
    }

    @RunWithoutAuthorization
    public boolean isBusinessRelationDataProvided(DelegateExecution execution) {
        var smeCase = getSmeCase(execution);
        var isBusinessRelationRequired = smeCase.getBusinessRelationRequired();
        if (Boolean.TRUE.equals(isBusinessRelationRequired)) {
            return Optional.ofNullable(smeCase.getBusinessRelations())
                .map(BusinessRelationsCaseData::getStatus)
                .filter(BusinessRelationsPredicates.isDataProvided())
                .isPresent();
        } else {
            return isBusinessRelationRequired != null;
        }
    }

    @RunWithoutAuthorization
    public String getBusinessSubtype(VariableContext variableContext) {
        var smeCase = getSmeCase(variableContext);

        return smeCase.getCompanyLookupInfo().getBusinessStructureInfo().getSubtype() == null ? null :
            smeCase.getCompanyLookupInfo().getBusinessStructureInfo().getSubtype().value();
    }

    @RunWithoutAuthorization
    public String getIndustry(VariableContext variableContext) {
        var smeCase = getSmeCase(variableContext);
        return smeCase.getCompanyLookupInfo().getBusinessIdentityInfo().getIndustries() == null ? null
            : smeCase.getCompanyLookupInfo().getBusinessIdentityInfo().getIndustries().get(0).getCode();
    }

    @RunWithoutAuthorization
    public String getOperationState(VariableContext variableContext) {
        var smeCase = getSmeCase(variableContext);

        return smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getStateOperatingIn();
    }

    @RunWithoutAuthorization
    public boolean hasKnownName(VariableContext variableContext) {
        return getCompanyLookupInfo(variableContext)
            .map(CompanyLookupInfo::getBusinessDetailsInfo)
            .map(BusinessDetails::getDba)
            .filter(StringUtils::isNotEmpty)
            .isPresent();
    }

    private Optional<CompanyLookupInfo> getCompanyLookupInfo(VariableContext variableContext) {
        return Optional.of(variableContext)
            .map(this::getSmeCase)
            .map(SmeCaseDefinition::getCompanyLookupInfo);
    }

    @RunWithoutAuthorization
    public String getDocumentType(VariableContext variableContext) {
        var variable = variableContext.resolve(CaseDefinitionConstants.DOCUMENT_REQUEST);

        if (variable == null || variable.getValue() == null || !(variable.getValue() instanceof Map)) {
            return null;
        }

        Map<String, String> documentRequest = (Map) variable.getValue();

        return documentRequest.get(CaseDefinitionConstants.DOCUMENT_TYPE);
    }

    @RunWithoutAuthorization
    public void applicantOnboardingPending(DelegateExecution execution, String applicantId) {
        var caseKey = CaseKey.from(execution).getKey();
        var event = new ApplicantOnboardingPendingEvent(caseKey, metadataWithApplicantId(applicantId));
        publisher.publish(caseKey, event);
    }

    @RunWithoutAuthorization
    public void applicantOnboardingFinished(DelegateExecution execution, String applicantId) {
        var caseKey = CaseKey.from(execution).getKey();
        var event = new ApplicantOnboardingFinishedEvent(caseKey, metadataWithApplicantId(applicantId));
        publisher.publish(caseKey, event);
    }

    @RunWithoutAuthorization
    public void additionalApplicantActionNotRequired(DelegateExecution execution, String applicantId) {
        var caseKey = CaseKey.from(execution).getKey();
        var event = new ApplicantActionNotRequiredEvent(caseKey, metadataWithApplicantId(applicantId));
        publisher.publish(caseKey, event);
    }


    @RunWithoutAuthorization
    public boolean riskAssessmentApproved(DelegateExecution execution) {
        return getRiskAssessmentStatus(execution) == RiskAssessmentCaseData.Status.APPROVED;
    }

    @RunWithoutAuthorization
    public boolean riskAssessmentRejected(DelegateExecution execution) {
        return getRiskAssessmentStatus(execution) == RiskAssessmentCaseData.Status.REJECTED;
    }

    private RiskAssessmentCaseData.Status getRiskAssessmentStatus(DelegateExecution execution) {
        return getSmeCase(execution).getRiskAssessment().getStatus();
    }

    private Map<String, String> metadataWithApplicantId(String userId) {
        return Map.of(APPLICANT_ID, userId);
    }

    private SmeCaseDefinition getSmeCase(DelegateExecution execution) {
        return getCase(execution).getCaseData(SmeCaseDefinition.class);
    }

    private SmeCaseDefinition getSmeCase(VariableContext variableContext) {
        var caseKey = variableContext.resolve(PROCESS_VARIABLE_CASE_KEY);
        return caseDataService.getCaseByKey(UUID.fromString((String) caseKey.getValue()))
            .getCaseData(SmeCaseDefinition.class);
    }

    private Case getCase(DelegateExecution execution) {
        return caseDataService.getCaseByKey(CaseKey.from(execution).getKey());
    }

    private void updateCase(DelegateExecution execution, Consumer<SmeCaseDefinition> caseUpdater) {
        var aCase = getCase(execution);
        var smeCase = aCase.getCaseData(SmeCaseDefinition.class);
        caseUpdater.accept(smeCase);
        aCase.setCaseData(smeCase);
        caseDataService.updateCase(aCase);
    }
}
