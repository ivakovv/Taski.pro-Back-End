package com.project.taskipro.controller;

import com.project.taskipro.dto.UserCredentialsResetDto;
import com.project.taskipro.service.UserCredentialsResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reset")
@RequiredArgsConstructor
public class UserCredentialsResetController {

    private final UserCredentialsResetService userCredentialsResetService;
    
    @PostMapping("/send-code-password")
    public ResponseEntity<String> sendCode(@RequestBody UserCredentialsResetDto request) {

        userCredentialsResetService.sendCodePassword(request);
        return ResponseEntity.ok("Код для сброса пароля отправлен");

    }

    @PostMapping("/send-code-email")
    public ResponseEntity<String> changeMail(@RequestBody UserCredentialsResetDto request){

        userCredentialsResetService.sendCodeMail(request);
        return ResponseEntity.ok("Код для сброса почты отправлен");

    }

}
