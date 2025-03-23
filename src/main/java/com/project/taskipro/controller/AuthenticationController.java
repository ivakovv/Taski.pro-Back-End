package com.project.taskipro.controller;

import com.project.taskipro.dto.*;
import com.project.taskipro.repository.UserRepository;
import com.project.taskipro.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final СonfirmationRegistration confirmationRegistration;


    @PostMapping("/registration")
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequestDto registrationDto) {

        //TO DO: вынести это в сервис
        if(userService.existsByUsername(registrationDto.username())) {
            return ResponseEntity.badRequest().body("Имя пользователя уже занято");
        }

        if(userService.existsByEmail(registrationDto.email())) {
            return ResponseEntity.badRequest().body("Email уже занят");
        }

        authenticationService.register(registrationDto);

        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authenticationService.refreshToken(request, response);
    }

    @PostMapping("/confirm-mail")
    public ResponseEntity<String> confirmMail(@RequestBody UserCredentialsResetDto request){

        confirmationRegistration.sendConfirmCode(request);
        return ResponseEntity.ok("Код для подверждения регистрации отправлен");

    }

}