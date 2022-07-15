package com.backbase.flow.onboarding.us.mapper;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.mapper.address.CoApplicantAwareAddressMapReader;
import com.backbase.flow.onboarding.us.mapper.address.CoApplicantAwareAddressMapWriter;
import com.backbase.flow.onboarding.us.mapper.applicant.CoApplicantAwareApplicantReader;
import com.backbase.flow.onboarding.us.mapper.applicant.CoApplicantAwareApplicantWriter;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingReader;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingWriter;
import com.backbase.flow.onboarding.us.mapper.otp.CoApplicantAwareOtpReader;
import com.backbase.flow.onboarding.us.mapper.otp.CoApplicantAwareOtpWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("rawtypes")
public class JourneyMapperAutoConfiguration {

    @Bean
    public JourneyMapper<Onboarding> onboardingJourneyMapper(List<JourneyReader<Onboarding>> readerList,
        List<JourneyWriter<Onboarding>> writerList, CaseDataService caseDataService) {
        return new JourneyMapper<>(Onboarding.class, readerList, writerList, caseDataService);
    }

    @Bean
    public JourneyMapper<Applicant> applicantJourneyMapper(List<JourneyReader<Applicant>> readerList,
        List<JourneyWriter<Applicant>> writerList, CaseDataService caseDataService) {
        return new JourneyMapper<>(Applicant.class, readerList, writerList, caseDataService);
    }

    @Bean
    public JourneyMapper<Map> journeyMapper(List<JourneyWriter<Map>> writerList, List<JourneyReader<Map>> readerList,
        CaseDataService caseDataService) {
        return new JourneyMapper(Map.class, readerList, writerList, caseDataService);
    }

    @Bean
    public JourneyWriter<Onboarding> onboardingJourneyWriter(CaseDataService caseDataService) {
        return new OnboardingWriter(caseDataService);
    }

    @Bean
    public JourneyReader<Onboarding> onboardingJourneyReader() {
        return new OnboardingReader();
    }

    @Bean
    public JourneyWriter<Applicant> applicantJourneyWriter(CaseDataService caseDataService) {
        return new CoApplicantAwareApplicantWriter(caseDataService);
    }

    @Bean
    public JourneyReader<Applicant> applicantJourneyReader() {
        return new CoApplicantAwareApplicantReader();
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.one-time-password.mapper",
        name = "impl", havingValue = "co_applicant_aware")
    public JourneyWriter<Map> coApplicantAwareOtpWriter(CaseDataService caseDataService) {
        return new CoApplicantAwareOtpWriter(caseDataService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.one-time-password.mapper",
        name = "impl", havingValue = "co_applicant_aware")
    public JourneyReader<Map> coApplicantAwareOtpReader() {
        return new CoApplicantAwareOtpReader();
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.address-validation.mapper",
        name = "impl", havingValue = "co_applicant_aware")
    public JourneyWriter<Map> coApplicantAwareAddressWriter(ObjectMapper objectMapper,
        CaseDataService caseDataService) {
        return new CoApplicantAwareAddressMapWriter(objectMapper, caseDataService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "backbase.flow.integrations.address-validation.mapper",
        name = "impl", havingValue = "co_applicant_aware")
    public JourneyReader<Map> coApplicantAwareAddressReader(ObjectMapper objectMapper) {
        return new CoApplicantAwareAddressMapReader(objectMapper);
    }

}
