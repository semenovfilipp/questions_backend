package org.semenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semenov.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void send(String emailTo, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(username);
            mailMessage.setTo(emailTo);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);

            log.info("Email sent successfully to {}", emailTo);
        } catch (Exception e) {
            log.error("Error occurred while sending email to {}: {}", emailTo, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
