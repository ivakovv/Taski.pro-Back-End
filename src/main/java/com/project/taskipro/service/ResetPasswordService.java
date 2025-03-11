package com.project.taskipro.service;

import com.project.taskipro.entity.PasswordReset;
import com.project.taskipro.entity.User;
import com.project.taskipro.repository.ResetPasswordRepository;
import com.project.taskipro.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

@Service
@RequiredArgsConstructor

@Slf4j
public class ResetPasswordService {

    private final UserRepository userRepository;

    private final ResetPasswordRepository resetPasswordRepository;

    public String loadResetCode(Long userId){

        String resetCode = this.generateCode();

        PasswordReset passwordReset = new PasswordReset();
        User user = userRepository.findById(userId).orElseThrow();

        passwordReset.setUser(user);
        passwordReset.setResetCode(resetCode);

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, 5);
        passwordReset.setResetCodeExpireTime(calendar);

        resetPasswordRepository.save(passwordReset);

        return resetCode;

    }

    public boolean isValidCode(Long userId, String resetCode){

        PasswordReset passwordReset = resetPasswordRepository.findByResetCode(resetCode).orElseThrow();

        return passwordReset.getUser().getId().equals(userId) &&
                passwordReset.getResetCode().equals(resetCode) &&
                passwordReset.getResetCodeExpireTime().after(new GregorianCalendar());
    }

    private String generateCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

}
