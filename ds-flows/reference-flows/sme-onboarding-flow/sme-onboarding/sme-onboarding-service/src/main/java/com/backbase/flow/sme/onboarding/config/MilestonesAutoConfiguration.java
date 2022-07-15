package com.backbase.flow.sme.onboarding.config;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.sme.onboarding.process.service.SmeDocumentRequestService;
import com.backbase.flow.sme.onboarding.statustracking.provider.AllDocsVerifiedMilestoneEventProvider;
import com.backbase.flow.sme.onboarding.statustracking.provider.ApplicationSubmittedMilestoneEventProvider;
import com.backbase.flow.sme.onboarding.statustracking.provider.DocumentRequestsCompletedMilestoneEventProvider;
import com.backbase.flow.sme.onboarding.statustracking.provider.DocumentsUploadCompletedMilestoneEventProvider;
import com.backbase.flow.sme.onboarding.statustracking.provider.NoDocsRequiredEventProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilestonesAutoConfiguration {

    @Bean
    public DocumentsUploadCompletedMilestoneEventProvider documentsUploadedMilestoneProvider(
        SmeDocumentRequestService documentRequestService, CaseEngineEventPublisher eventPublisher,
        FlowSecurityContext flowSecurityContext
    ) {
        return new DocumentsUploadCompletedMilestoneEventProvider(
            documentRequestService, eventPublisher, flowSecurityContext
        );
    }

    @Bean
    public ApplicationSubmittedMilestoneEventProvider applicationSubmittedMilestoneEventProvider(
        CaseEngineEventPublisher eventPublisher
    ) {
        return new ApplicationSubmittedMilestoneEventProvider(eventPublisher);
    }

    @Bean
    public NoDocsRequiredEventProvider noDocsRequiredMilestoneProvider(CaseEngineEventPublisher eventPublisher) {
        return new NoDocsRequiredEventProvider(eventPublisher);
    }

    @Bean
    public AllDocsVerifiedMilestoneEventProvider allDocsVerifiedMilestoneProvider(
        CaseEngineEventPublisher eventPublisher
    ) {
        return new AllDocsVerifiedMilestoneEventProvider(eventPublisher);
    }

    @Bean
    public DocumentRequestsCompletedMilestoneEventProvider applicationCompletedMilestoneEventProvider(
        CaseEngineEventPublisher eventPublisher
    ) {
        return new DocumentRequestsCompletedMilestoneEventProvider(eventPublisher);
    }
}
