package com.project.taskipro.service;

import com.project.taskipro.entity.PasswordReset;
import com.project.taskipro.entity.User;
import com.project.taskipro.repository.ResetPasswordRepository;
import com.project.taskipro.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailSendlerService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    private final ResetPasswordRepository resetPasswordRepository;


    public void send(String to, String subject, String body, Long userId, String resetCode) {
        try {

            PasswordReset passwordReset = new PasswordReset();
            User user = userRepository.findById(userId)
                    .orElseThrow();
            passwordReset.setUser(user);
            passwordReset.setResetCode(resetCode);
            resetPasswordRepository.save(passwordReset);



            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(from);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Ошибка при отправке письма: " + e.getMessage());
            throw new RuntimeException("Ошибка при отправке письма", e);
        }
    }

    public String generateCode(){
        StringBuilder resetCode = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 6; i++){
            int num = random.nextInt(10);

            resetCode.append(num);
        }
        return resetCode.toString();
    }
}