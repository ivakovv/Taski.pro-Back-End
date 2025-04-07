package com.project.taskipro.service;

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

    public String getBackgroundColor(){

        Long userId = userService.getCurrentUser().getId();

        BackGroundColor backGroundColor = backGroundColorsRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Для пользователя с id %d не найден фон", userId)));
        return backGroundColor.getColorCode();

    }

    public String getTheme(){
        Long userId = userService.getCurrentUser().getId();

        BackGroundColor backGroundColor = backGroundColorsRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Для пользователя с id %d не найден фон", userId)));
        return backGroundColor.getTheme();
    }

    public void setBackgroundColor(String colorCode) {

        Long userId = userService.getCurrentUser().getId();

        BackGroundColor backGroundColor = backGroundColorsRepository.findById(userId)
                .orElse(new BackGroundColor());

        backGroundColor.setUserId(userId);
        backGroundColor.setColorCode(colorCode);
        backGroundColorsRepository.save(backGroundColor);
    }

    public void setTheme(String theme){
        Long userId = userService.getCurrentUser().getId();

        BackGroundColor backGroundColor = backGroundColorsRepository.findById(userId)
                .orElse(new BackGroundColor());
        backGroundColor.setUserId(userId);
        backGroundColor.setTheme(theme);
        backGroundColorsRepository.save(backGroundColor);
    }

}
