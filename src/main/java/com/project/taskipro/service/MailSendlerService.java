package com.project.taskipro.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSendlerService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(from);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Ошибка при отправке письма: {}", e.getMessage());
            throw new RuntimeException("Ошибка при отправке письма", e);
        }
    }

}