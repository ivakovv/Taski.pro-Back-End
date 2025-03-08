package com.project.taskipro.controller;

import com.project.taskipro.dto.MailRequestDto;
import com.project.taskipro.service.EmailSendlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final EmailSendlerService mailSendler;

    @GetMapping("/send")
    public void sendMail(@RequestBody MailRequestDto request){
        //Генерим код для сброса пароля
        String resetCode = mailSendler.generateCode();

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"ru\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Template</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            color: #333;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #007BFF;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p {\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        .code {\n" +
                "            text-align: center;\n" +
                "            font-size: 24pt;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 14px;\n" +
                "            text-align: right;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "        .message {\n" +
                "            text-align: center;\n" +
                "            font-size: 14pt;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Ваш код для восстановления пароля</h1>\n" +
                "        <p class=\"code\">" + resetCode + "</p>\n" +
                "        <p class=\"message\">Никому не сообщайте данный код!</p>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>С уважением,<br>Команда Taski.pro</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        mailSendler.send(
                "ivakov.andrey.2004@mail.ru",
                "Здарвствуйте, ваш код для восстановления пароля: " + resetCode,
                htmlContent,
                request.getUserId(),
                resetCode
        );
    }
}
