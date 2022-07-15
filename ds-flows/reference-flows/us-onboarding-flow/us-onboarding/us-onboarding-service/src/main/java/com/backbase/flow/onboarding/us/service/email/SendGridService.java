package com.backbase.flow.onboarding.us.service.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendGridService {

    public static final String MAIL_SEND_ENDPOINT = "mail/send";

    @Value("${backbase.flow.integrations.email.sendgrid.api-key}")
    private String apiKey;
    @Value("${backbase.flow.integrations.email.sendgrid.email-from}")
    private String emailFrom;
    @Value("${backbase.flow.integrations.email.impl:MOCK}")
    private EmailImplementation implementation;

    private SendGrid sendGrid;

    @PostConstruct
    public void init() {
        sendGrid = new SendGrid(apiKey);
    }

    public void sendEmail(EmailDto emailDto) {
        if (EmailImplementation.MOCK.equals(implementation)) {
            return;
        }

        var mail = new Mail();

        var personalization = new Personalization();
        personalization.addTo(new Email(emailDto.getTo()));
        emailDto.getTemplateData().forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        mail.setTemplateId(emailDto.getTemplateId());
        mail.setFrom(new Email(emailFrom));

        var request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_SEND_ENDPOINT);
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            log.error("Email sending with templateId: {} for to {} has failed", emailDto.getTemplateId(),
                emailDto.getTo(), ex);
        }
    }

}
