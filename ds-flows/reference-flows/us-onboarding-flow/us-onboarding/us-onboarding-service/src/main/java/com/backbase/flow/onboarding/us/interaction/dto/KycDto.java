package com.backbase.flow.onboarding.us.interaction.dto;

import com.backbase.flow.onboarding.us.kyc.dto.AnswerDto;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycDto {
    @NotEmpty(message = "Answers can't be empty")
    private List<AnswerDto> answers;
}
