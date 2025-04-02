package com.project.taskipro.scheduler;

import com.project.taskipro.model.codes.Code;
import com.project.taskipro.repository.CodesRepository;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@EnableScheduling
@AllArgsConstructor
public class DeleteExpiredCodes {

    private final CodesRepository codesRepository;

    @Scheduled(cron = "${app.schedule.cron}")
    public void deleteExpiredCodes() {
        List<Code> listCodeCodes = codesRepository.getAllByCodeExpireTimeBefore(LocalDateTime.now().plusMinutes(5));
        if(!listCodeCodes.isEmpty()){
            codesRepository.deleteAll(listCodeCodes);
        }
    }
}
