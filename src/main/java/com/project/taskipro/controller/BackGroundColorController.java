package com.project.taskipro.controller;

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
    public ResponseEntity<String> getBackgroundColor(){
        return ResponseEntity.ok().body(backGroundColorService.getBackgroundColor());
    }
    @PostMapping
    public ResponseEntity<Void> setBackgroundColor(@RequestParam String colorCode) {
        backGroundColorService.setBackgroundColor(colorCode);
        return ResponseEntity.ok().build();
    }

}
