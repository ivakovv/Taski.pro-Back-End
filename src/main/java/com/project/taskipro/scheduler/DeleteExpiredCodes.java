package com.project.taskipro.scheduler;

import com.project.taskipro.entity.PasswordReset;
import com.project.taskipro.repository.ResetPasswordRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@Component
@EnableScheduling

@AllArgsConstructor
public class DeleteExpiredCodes {

    private final ResetPasswordRepository resetPasswordRepository;

    @Scheduled(cron = "0 */30 * * * *")
    public void deleteExpiredCodes(){

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, 5);

        List<PasswordReset> list = resetPasswordRepository.getAllByResetCodeExpireTimeBefore(calendar);
        resetPasswordRepository.deleteAll(list);
    }
}
