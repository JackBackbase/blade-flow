package com.backbase.flow.sme.onboarding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "backbase.sme-onboarding.customer")
public class CustomerConfigurationProperties {

    private String externalLegalEntity;
}
