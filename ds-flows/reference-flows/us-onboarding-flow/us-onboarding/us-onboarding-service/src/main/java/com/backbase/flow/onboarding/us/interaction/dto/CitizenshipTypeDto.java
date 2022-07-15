package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.CITIZENSHIP_NULL_MESSAGE;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenshipTypeDto {

    @NotNull(message = CITIZENSHIP_NULL_MESSAGE)
    private CitizenshipType type;
}
