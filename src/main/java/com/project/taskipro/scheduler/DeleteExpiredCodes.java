package com.project.taskipro.scheduler;

import com.project.taskipro.model.codes.Code;
import com.project.taskipro.repository.CodesRepository;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@EnableScheduling
public class DeleteExpiredCodes {

//    @Value("${app.schedule.cron}")
//    private final String cronValue;

    private final CodesRepository codesRepository;

    public DeleteExpiredCodes(CodesRepository codesRepository) {
        //this.cronValue = cronValue;
        this.codesRepository = codesRepository;
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void deleteExpiredCodes() {
        List<Code> listCodeCodes = codesRepository.getAllByCodeExpireTimeBefore(LocalDateTime.now().plusMinutes(5));
        if(!listCodeCodes.isEmpty()){
            codesRepository.deleteAll(listCodeCodes);
        }
    }
}
