package com.project.taskipro.service;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.dto.UserCredentialsResetDto;
import com.project.taskipro.entity.CodeType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class СonfirmationRegistrationService {

    private final MailSendlerService mailSendler;

    private final CodesService codesService;

    private final UserRepository userRepository;

    public void sendConfirmCode(UserCredentialsResetDto request) {

        String code = getCode(getUser(request.userId()).getId(), CodeType.CONFIRM_MAIL);

        mailSendler.sendMail(getUser(request.userId()).getEmail(), "Здарвствуйте, " +
                "ваш код для подверждения регистрации: " + code, getMailContent(code, "mail"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    private String getCode(Long userId, CodeType codeType) {
        return codesService.loadCode(getUser(userId).getId(), codeType);
    }

    private String getMailContent(String code, String htmlTemplate) {
        return htmlTemplate == "password" ? Constants.getHtmlPasswordTemplate(code)
                : Constants.getHtmlChangeMailTemplate(code);
    }
}
