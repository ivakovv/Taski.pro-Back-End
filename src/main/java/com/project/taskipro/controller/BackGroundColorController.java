package com.project.taskipro.controller;

import com.project.taskipro.constants.Constants;
import com.project.taskipro.service.BackGroundColorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/background-colors")
public class BackGroundColorController {

    private final BackGroundColorService backGroundColorService;

    @GetMapping("/color")
    public ResponseEntity<String> getBackgroundColor(){
        return ResponseEntity.ok().body(backGroundColorService.getBackgroundColor());
    }
    @PutMapping("/color")
    public ResponseEntity<String> setBackgroundColor(@RequestParam String colorCode) {
        if(Arrays.stream(Constants.colors).noneMatch(s -> s.equals(colorCode))){
            return ResponseEntity.badRequest().body("Неверный формат цвета");
        }
        backGroundColorService.setBackgroundColor(colorCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/theme")
    public ResponseEntity<String> getTheme(){
        return ResponseEntity.ok().body(backGroundColorService.getTheme());
    }

    @PutMapping("/theme")
    public ResponseEntity<String> setTheme(@RequestParam String theme){
        if(Arrays.stream(Constants.themes).noneMatch(s -> s.equals(theme))){
            return ResponseEntity.badRequest().body("Неверный формат темы");
        }
        backGroundColorService.setTheme(theme);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/bg-theme")
    public ResponseEntity<Integer> getBgTheme(){
        return ResponseEntity.ok().body(backGroundColorService.getBgTheme());
    }
    @PutMapping("/bg-theme")
    public ResponseEntity<Void> setBgTheme(@RequestParam Integer bgTheme){
        backGroundColorService.setBgTheme(bgTheme);
        return ResponseEntity.ok().build();
    }

}
