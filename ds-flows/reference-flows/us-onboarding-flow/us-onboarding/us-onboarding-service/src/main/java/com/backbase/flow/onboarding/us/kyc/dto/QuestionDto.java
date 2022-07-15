package com.backbase.flow.onboarding.us.kyc.dto;

import java.util.List;
import lombok.Data;

@Data
public class QuestionDto {
    private String id;
    private String label;
    private QuestionType type;
    private List<String> options;
}
