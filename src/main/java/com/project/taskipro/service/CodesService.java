package com.project.taskipro.service;

import com.project.taskipro.model.codes.Code;
import com.project.taskipro.model.codes.CodeType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.CodesRepository;
import com.project.taskipro.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor

@Slf4j
public class CodesService {

    private final UserServiceImpl userService;

    private final CodesRepository codesRepository;

    public String loadCode(User user, CodeType codeType){

        String generateCode = this.generateCode();

        Code code = new Code();

        code.setUser(user);
        code.setCode(generateCode);
        code.setCodeType(codeType);

        LocalDateTime currentTime = LocalDateTime.now();
        code.setCodeExpireTime(currentTime.plusMinutes(5));

        codesRepository.save(code);

        return generateCode;

    }

    public boolean isValidCode(String email, String resetCode, CodeType type){

        Code code = codesRepository.findByCode(resetCode).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный код не найден!"));

        return code.getUser().getId().equals(userService.getUserByMail(email).getId()) &&
                code.getCode().equals(resetCode) &&
                code.getCodeExpireTime().isAfter(LocalDateTime.now()) &&
                code.getCodeType().equals(type);
    }

    private String generateCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

}
