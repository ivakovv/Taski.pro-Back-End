package com.project.taskipro.service;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.model.codes.CodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserCredentialsResetService {

    private final MailSendlerService mailSendler;

    private final CodesService codesService;

    private final UserServiceImpl userService;

    public void sendCodeMail() {

        String code = loadCode(CodeType.RESET_MAIL);

        mailSendler.sendMail(userService.getCurrentUser().getEmail(), "Здарвствуйте, " +
                "ваш код для изменения почты: " + code, getMailContent(code, "mail"));
    }

    public void sendCodePassword() {

        String code = loadCode(CodeType.RESET_PASSWORD);

        mailSendler.sendMail(userService.getCurrentUser().getEmail(), "Здарвствуйте, " +
                "ваш код для сброса пароля: " + code, getMailContent(code, "password"));
    }


    private String loadCode(CodeType codeType) {
        return codesService.loadCode(codeType);
    }

    private String getMailContent(String code, String htmlTemplate) {
        return htmlTemplate.equals("password") ? Constants.getHtmlPasswordTemplate(code)
                : Constants.getHtmlChangeMailTemplate(code);
    }
}
