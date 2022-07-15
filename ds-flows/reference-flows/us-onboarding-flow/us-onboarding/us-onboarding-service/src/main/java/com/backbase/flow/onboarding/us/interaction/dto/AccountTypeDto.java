package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.JOINT_ACCOUNT_NULL_MESSAGE;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeDto {

    @NotNull(message = JOINT_ACCOUNT_NULL_MESSAGE)
    private Boolean isJointAccount;
}
