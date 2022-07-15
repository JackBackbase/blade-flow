package com.backbase.flow.onboarding.us.service.email;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDto {

    private final String to;
    private final String templateId;
    private final Map<String, String> templateData;
}
