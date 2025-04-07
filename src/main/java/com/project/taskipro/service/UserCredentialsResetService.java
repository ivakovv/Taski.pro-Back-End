package com.project.taskipro.service;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.model.codes.CodeType;
import com.project.taskipro.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserCredentialsResetService {

    private final MailSendlerService mailSendler;

    private final CodesService codesService;

    private final UserServiceImpl userService;

    public void sendCodeMail() {

        String code = loadCode(CodeType.RESET_MAIL, null);

        mailSendler.sendMail(userService.getCurrentUser().getEmail(), String.format("Здарвствуйте, ваш код для изменения почты: %s", code), getMailContent(code, "mail"));
    }

    public void sendCodePassword(String email) {

        String code = loadCode(CodeType.RESET_PASSWORD, email);

        mailSendler.sendMail(email, String.format("Здарвствуйте, ваш код для изменения пароля: %s", code), getMailContent(code, "password"));
    }


    private String loadCode(CodeType codeType, String email) {
        User user = (codeType == CodeType.RESET_PASSWORD)
                ? userService.getUserByMail(email)
                : userService.getCurrentUser();
        return codesService.loadCode(user, codeType);
    }

    private String getMailContent(String code, String htmlTemplate) {
        return htmlTemplate.equals("password") ? Constants.getHtmlPasswordTemplate(code)
                : Constants.getHtmlChangeMailTemplate(code);
    }
}
