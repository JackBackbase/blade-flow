package com.backbase.flow.onboarding.us.interaction.dto.response;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchCitizenshipDataResponseDto {

    private CitizenshipType type;
    private String ssn;
    private NonResidentDataDto nonResident;

}
