package com.savemyreceipt.smr.utils;

import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Receipt;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendGridUtil {

    private final SendGrid sendGrid;

    @Value("${spring.sendgrid.from}")
    private String fromEmail;

    @Value("${spring.sendgrid.template-id}")
    private String templateId;

    // Sendgrid 공식 가이드 참고
    // https://github.com/sendgrid/sendgrid-java
    public void sendEmail() throws IOException {
        Email from = new Email(fromEmail);
        String subject = "Sending with Twilio SendGrid is Fun";
        Email to = new Email("kmicety1@gmail.com");
        Content content = new Content("text/plain", "and easy to learn");

        Mail mail = new Mail(from, subject, to, content);
        Email email = new Email("yelnets99@naver.com");
        mail.personalization.get(0).addTo(email);

        send(mail);
    }

    public void sendDynamicTemplateEmail(Member member, Receipt receipt) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(member.getEmail());
        Mail mail = new Mail();

        mail.setFrom(from);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("accountant_name", member.getName());
        personalization.addDynamicTemplateData("group_name", receipt.getGroup().getName());
        personalization.addDynamicTemplateData("receipt_owner_name", receipt.getMember().getName());
        personalization.addDynamicTemplateData("category", receipt.getCategory());
        personalization.addDynamicTemplateData("total_price", receipt.getPrice());
        personalization.addDynamicTemplateData("purchased_at", receipt.getPurchasedAt().format(
            DateTimeFormatter.ISO_LOCAL_DATE));
        personalization.addTo(to);

        mail.addPersonalization(personalization);

        send(mail);
    }

    private void send(Mail mail) throws IOException {
        sendGrid.addRequestHeader("X-Mock", "true");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        log.info("SendGrid Response: {}", response.getStatusCode());
        log.info("SendGrid Response: {}", response.getBody());
        log.info("SendGrid Response: {}", response.getHeaders());
    }
}
