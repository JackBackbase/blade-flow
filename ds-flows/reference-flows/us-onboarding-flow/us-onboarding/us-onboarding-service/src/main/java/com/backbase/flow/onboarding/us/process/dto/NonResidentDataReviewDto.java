package com.backbase.flow.onboarding.us.process.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.APPROVED_NULL_MESSAGE;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonResidentDataReviewDto {

    @NotNull(message = APPROVED_NULL_MESSAGE)
    private Boolean approved;
    private String comment;
}
