package com.backbase.flow.onboarding.us.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.onboarding.us.mapper.address.CoApplicantAwareAddressMapReader;
import com.backbase.flow.onboarding.us.mapper.address.CoApplicantAwareAddressMapWriter;
import com.backbase.flow.onboarding.us.mapper.otp.CoApplicantAwareOtpReader;
import com.backbase.flow.onboarding.us.mapper.otp.CoApplicantAwareOtpWriter;
import com.backbase.flow.service.mapper.DefaultAddressMapReader;
import com.backbase.flow.service.mapper.DefaultAddressMapWriter;
import com.backbase.flow.service.mapper.DefaultOtpReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

class JourneyMapperAutoConfigurationTest {

    public static final String CUSTOM_ADDRESS_MAPPER = "backbase.flow.integrations.address-validation.mapper.impl=co_applicant_aware";
    public static final String CUSTOM_OTP_MAPPER = "backbase.flow.integrations.one-time-password.mapper.impl=co_applicant_aware";

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withInitializer(new ConditionEvaluationReportLoggingListener())
        .withUserConfiguration(JourneyMapperAutoConfiguration.class)
        .withConfiguration(AutoConfigurations.of(TestConfig.class));

    @Test
    void coApplicantAware() {
        contextRunner
            .withPropertyValues(CUSTOM_ADDRESS_MAPPER, CUSTOM_OTP_MAPPER)
            .run(context -> assertAll(
                () -> assertThat(context).hasSingleBean(CoApplicantAwareAddressMapReader.class),
                () -> assertThat(context).hasSingleBean(CoApplicantAwareAddressMapWriter.class),
                () -> assertThat(context).hasSingleBean(CoApplicantAwareOtpReader.class),
                () -> assertThat(context).hasSingleBean(CoApplicantAwareOtpWriter.class),

                () -> assertThat(context).doesNotHaveBean(DefaultAddressMapWriter.class),
                () -> assertThat(context).doesNotHaveBean(DefaultAddressMapReader.class),
                () -> assertThat(context).doesNotHaveBean(DefaultAddressMapReader.class),
                () -> assertThat(context).doesNotHaveBean(DefaultOtpReader.class),
                () -> assertThat(context).doesNotHaveBean(DefaultOtpReader.class)));
    }

    static class TestConfig {

        @Bean
        public CaseDataService mockCaseDataService() {
            return mock(CaseDataService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }
}
