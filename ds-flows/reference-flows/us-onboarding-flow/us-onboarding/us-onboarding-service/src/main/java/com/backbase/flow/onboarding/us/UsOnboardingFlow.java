package com.backbase.flow.onboarding.us;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
public class UsOnboardingFlow {

    public static void main(String[] args) {
        SpringApplication.run(UsOnboardingFlow.class, args);
    }
}
