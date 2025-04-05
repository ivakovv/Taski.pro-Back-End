package com.project.taskipro.service;

import com.project.taskipro.dto.BackgroundRequestDto;
import com.project.taskipro.model.user.BackGroundColor;
import com.project.taskipro.repository.BackGroundColorsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class BackGroundColorService {

    private final BackGroundColorsRepository backGroundColorsRepository;

    private final UserServiceImpl userService;

    public String getBackgroundColor(BackgroundRequestDto request){

        BackGroundColor backGroundColor = getBackgroundColor(request.userId());
        return backGroundColor.getColorCode();

    }

    public void setBackgroundColor(BackgroundRequestDto request) {

        userService.getUserById(request.userId());

        BackGroundColor backGroundColor = backGroundColorsRepository.findById(request.userId())
                .orElse(new BackGroundColor());

        backGroundColor.setUserId(request.userId());
        backGroundColor.setColorCode(request.colorCode());
        backGroundColorsRepository.save(backGroundColor);
    }

    public BackGroundColor getBackgroundColor(Long userId){
        return backGroundColorsRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Для пользователя с id %d не найден фон", userId)));
    }
}
