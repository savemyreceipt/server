package com.savemyreceipt.smr.config;

import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    @Bean
    public SendGrid sendGrid() {
        if (apiKey == null) {
            throw new CustomException(ErrorStatus.BAD_REQUEST, "SendGrid integration is not enabled");
        }
        return new SendGrid(apiKey);
    }
}
