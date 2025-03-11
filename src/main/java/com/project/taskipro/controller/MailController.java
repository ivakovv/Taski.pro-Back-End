package com.project.taskipro.controller;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.dto.MailRequestDto;
import com.project.taskipro.dto.MailtTestDto;
import com.project.taskipro.entity.User;
import com.project.taskipro.repository.UserRepository;
import com.project.taskipro.service.MailSendlerService;
import com.project.taskipro.service.ResetPasswordService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mail")

@RequiredArgsConstructor
public class MailController {

    private final MailSendlerService mailSendler;

    private final ResetPasswordService resetPasswordService;

    private final UserRepository userRepository;


    @GetMapping("/send-reset-password")
    public void sendResetCode(@RequestBody MailtTestDto request){

        User user = userRepository.findById(request.userId()).orElseThrow();

        String resetCode = resetPasswordService.loadResetCode(user.getId());

        String mailContent = Constants.getHtmlPasswordResetTemplate(resetCode);

        mailSendler.sendMail(
                user.getEmail(),
                "Здарвствуйте, ваш код для восстановления пароля: " + resetCode,
                mailContent
        );
    }
}
