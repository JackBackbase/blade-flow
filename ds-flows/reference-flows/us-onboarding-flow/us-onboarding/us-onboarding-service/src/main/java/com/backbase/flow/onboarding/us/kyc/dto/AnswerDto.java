package com.backbase.flow.onboarding.us.kyc.dto;

import java.util.List;
import lombok.Data;

@Data
public class AnswerDto {
    private String id;
    private List<String> selectedOptions;
}
