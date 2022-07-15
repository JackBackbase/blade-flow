package com.backbase.flow.onboarding.us.process.dto;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenshipUserTaskInfoDto {

    private String fullName;
    private String dateOfBirth;
    private CitizenshipInfo citizenship;
}
