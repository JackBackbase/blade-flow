package com.backbase.flow.sme.onboarding;

import com.backbase.flow.sme.onboarding.config.CustomerConfigurationProperties;
import com.backbase.flow.sme.onboarding.config.IdentityConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CustomerConfigurationProperties.class, IdentityConfigurationProperties.class})
public class SmeOnboardingFlow {

    public static void main(String[] args) {
        SpringApplication.run(SmeOnboardingFlow.class, args);
    }
}
