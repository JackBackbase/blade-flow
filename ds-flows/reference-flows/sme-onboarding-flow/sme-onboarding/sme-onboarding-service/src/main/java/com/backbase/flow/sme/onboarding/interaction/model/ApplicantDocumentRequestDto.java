package com.backbase.flow.sme.onboarding.interaction.model;

import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicantDocumentRequestDto {

    private Applicant applicant;
    private List<DocumentRequest> documentRequests;
}
