package com.backbase.flow.sme.onboarding.interaction.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LandingResponseDto {

    private UUID caseId;
    private String email;

}
