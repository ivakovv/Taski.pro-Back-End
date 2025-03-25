package com.project.taskipro.controller;

import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.service.UserFieldsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class UserFieldsController {

    private final UserFieldsService userFieldsService;

    public ResponseEntity<String> setUserFields(UserFieldsDto request){

        userFieldsService.setUserFields(request);
        return ResponseEntity.ok("Поля пользователя изменены");

    }


}
