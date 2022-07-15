package com.backbase.flow.sme.onboarding.config;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.sme.onboarding.data.OtpJourneyReader;
import com.backbase.flow.sme.onboarding.data.OtpJourneyWriter;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("rawtypes")
@Configuration
public class OtpJourneyAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.one-time-password.mapper", name = "impl", havingValue = "otpJourneyMapper")
    public JourneyWriter<Map> otpJourneyWriter(CaseDataService caseDataService) {
        return new OtpJourneyWriter(caseDataService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.one-time-password.mapper", name = "impl", havingValue = "otpJourneyMapper")
    public JourneyReader<Map> otpJourneyReader() {
        return new OtpJourneyReader();
    }
}
