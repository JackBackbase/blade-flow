package com.backbase.flow.onboarding.us.service.coapplicant;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.onboarding.us.service.email.EmailDto;
import com.backbase.flow.onboarding.us.service.email.SendGridService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CoApplicantNotificationService {

    @Value("${backbase.flow.co-applicant.notification.base-link.web}")
    private String baseLinkWeb;
    @Value("${backbase.flow.co-applicant.notification.base-link.ios}")
    private String baseLinkIOS;
    @Value("${backbase.flow.co-applicant.notification.base-link.android}")
    private String baseLinkAndroid;
    @Value("${backbase.flow.co-applicant.notification.template-id}")
    private String templateId;

    private final SendGridService sendGridService;

    public void sendEmail(Onboarding onboarding) {
        var emailDto = EmailDto.builder()
            .to(onboarding.getCoApplicant().getEmail())
            .templateId(templateId)
            .templateData(
                Map.of(
                    "co_applicant_name", onboarding.getCoApplicant().getFirstName(),
                    "main_applicant_name", onboarding.getMainApplicant().getFirstName(),
                    "product_name", "Current Account", //todo replace when there is a product choice
                    "ios_link", baseLinkIOS + onboarding.getCoApplicantId(),
                    "android_link", baseLinkAndroid + onboarding.getCoApplicantId(),
                    "web_link", baseLinkWeb + onboarding.getCoApplicantId()
                ))
            .build();

        sendGridService.sendEmail(emailDto);
    }
}
