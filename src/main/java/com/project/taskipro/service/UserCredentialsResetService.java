package com.project.taskipro.service;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.dto.UserCredentialsResetDto;
import com.project.taskipro.model.codes.CodeType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@Service
public class UserCredentialsResetService {

    private final MailSendlerService mailSendler;

    private final CodesService codesService;

    private final UserRepository userRepository;

    public void sendCodeMail(Long id) {

        String code = getCode(getUser(id).getId(), CodeType.RESET_MAIL);

        mailSendler.sendMail(getUser(id).getEmail(), "Здарвствуйте, " +
                "ваш код для изменения почты: " + code, getMailContent(code, "mail"));
    }

    public void sendCodePassword(Long id) {

        String code = getCode(getUser(id).getId(), CodeType.RESET_PASSWORD);

        mailSendler.sendMail(getUser(id).getEmail(), "Здарвствуйте, " +
                "ваш код для сброса пароля: " + code, getMailContent(code, "password"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не найден!"));
    }

    private String getCode(Long userId, CodeType codeType) {
        return codesService.loadCode(getUser(userId).getId(), codeType);
    }

    private String getMailContent(String code, String htmlTemplate) {
        return htmlTemplate == "password" ? Constants.getHtmlPasswordTemplate(code)
                : Constants.getHtmlChangeMailTemplate(code);
    }
}
