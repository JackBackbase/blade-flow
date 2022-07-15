package com.backbase.flow.sme.onboarding.interaction.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskDto {
    private String taskId;
    private String applicantId;
}
