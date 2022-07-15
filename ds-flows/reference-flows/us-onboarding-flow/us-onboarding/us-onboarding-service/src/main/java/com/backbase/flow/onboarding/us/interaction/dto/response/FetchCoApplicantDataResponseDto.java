package com.backbase.flow.onboarding.us.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchCoApplicantDataResponseDto {

    private Applicant mainApplicant;
    private Applicant coApplicant;
}
