package com.project.taskipro.service;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.model.codes.CodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmationRegistrationService {

    private final MailSendlerService mailSendler;

    private final CodesService codesService;

    private final UserServiceImpl userService;

    public void sendConfirmCode() {

        String code = loadCode(CodeType.CONFIRM_MAIL);

        mailSendler.sendMail(userService.getCurrentUser().getEmail(), String.format("Здарвствуйте, ваш код для подверждения регистрации: %s", code), getMailContent(code, "mail"));
    }

    private String loadCode(CodeType codeType) {
        return codesService.loadCode(userService.getCurrentUser(), codeType);
    }

    private String getMailContent(String code, String htmlTemplate) {
        return htmlTemplate.equals("password") ? Constants.getHtmlPasswordTemplate(code)
                : Constants.getHtmlChangeMailTemplate(code);
    }
}
