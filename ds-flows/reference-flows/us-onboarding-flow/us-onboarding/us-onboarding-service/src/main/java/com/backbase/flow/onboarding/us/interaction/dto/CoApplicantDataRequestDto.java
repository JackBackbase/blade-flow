package com.backbase.flow.onboarding.us.interaction.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoApplicantDataRequestDto {

    private UUID coApplicantId;
}
