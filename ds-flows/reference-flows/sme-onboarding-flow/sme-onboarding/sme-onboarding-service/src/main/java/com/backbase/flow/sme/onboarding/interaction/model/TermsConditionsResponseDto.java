package com.backbase.flow.sme.onboarding.interaction.model;

import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class TermsConditionsResponseDto {

    boolean accepted;
    OffsetDateTime acceptanceDate;
    boolean anchorDataReceived;
    String caseKey;
}
