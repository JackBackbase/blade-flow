package com.backbase.flow.sme.onboarding.config;

import com.backbase.flow.sme.onboarding.event.factory.SmeDocumentRequestNameOnlyEventFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EventLogAutoConfiguration {

    @Bean
    @Primary
    public SmeDocumentRequestNameOnlyEventFactory smeDocumentRequestNameOnlyEventFactory() {
        return new SmeDocumentRequestNameOnlyEventFactory();
    }
}
