package com.project.taskipro.controller;

import com.project.taskipro.dto.BackgroundRequestDto;
import com.project.taskipro.service.BackGroundColorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/background-colors")
public class BackGroundColorController {

    private final BackGroundColorService backGroundColorService;

    @GetMapping
    public ResponseEntity<String> getBackgroundColor(@RequestBody BackgroundRequestDto request){
        return ResponseEntity.ok().body(backGroundColorService.getBackgroundColor(request));
    }
    @PostMapping
    public ResponseEntity<Void> setBackgroundColor(@RequestBody BackgroundRequestDto request){
        backGroundColorService.setBackgroundColor(request);
        return ResponseEntity.ok().build();
    }

}
