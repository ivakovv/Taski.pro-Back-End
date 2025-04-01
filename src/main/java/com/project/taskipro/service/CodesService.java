package com.project.taskipro.service;

import com.project.taskipro.entity.Code;
import com.project.taskipro.entity.CodeType;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.CodesRepository;
import com.project.taskipro.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor

@Slf4j
public class CodesService {

    private final UserRepository userRepository;

    private final CodesRepository codesRepository;

    public String loadCode(Long userId, CodeType codeType){

        String generateCode = this.generateCode();

        Code code = new Code();
        User user = userRepository.findById(userId).orElseThrow();

        code.setUser(user);
        code.setCode(generateCode);
        code.setCodeType(codeType);

        LocalDateTime currentTime = LocalDateTime.now();
        code.setCodeExpireTime(currentTime.plusMinutes(5));

        codesRepository.save(code);

        return generateCode;

    }

    public boolean isValidCode(Long userId, String resetCode){

        Code code = codesRepository.findByCode(resetCode).orElseThrow();

        return code.getUser().getId().equals(userId) &&
                code.getCode().equals(resetCode) &&
                code.getCodeExpireTime().isAfter(LocalDateTime.now());
    }

    private String generateCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

}
