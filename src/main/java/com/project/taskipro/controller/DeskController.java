package com.project.taskipro.controller;

import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.dto.desk.DeskUpdateDto;
import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.model.desks.RightType;
import com.project.taskipro.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/desk")
public class DeskController {
    private final DeskService deskService;

    @PostMapping("/create")
    public ResponseEntity<String> createDesk(@RequestBody DeskCreateDto deskCreateDto){
        deskService.createDesk(deskCreateDto);
        return ResponseEntity.ok("Доска успешно создана!");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDesk(@PathVariable Long id){
        deskService.deleteDesk(id);
        return ResponseEntity.ok("Доска успешно удалена!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDesk(@PathVariable Long id, @RequestBody DeskUpdateDto deskUpdateDto){
        deskService.updateDesk(id, deskUpdateDto);
        return ResponseEntity.ok("Доска успешно обновлена!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeskResponseDto> getDeskById(@PathVariable Long id){
        Desks desk = deskService.getDeskById(id);
        return ResponseEntity.ok(deskService.mapToDeskResponseDto(desk));
    }

    @GetMapping
    public ResponseEntity<List<DeskResponseDto>> getDesksForUser(){
        List<DeskResponseDto> desks = deskService.getDesksForUser();
        return ResponseEntity.ok(desks);
    }

    //Будет ли установка прав при добавлении пользователя???
    @PostMapping("/{id}/users/{userid}")
    public ResponseEntity<String> addUserOnDesk(@PathVariable("id") Long deskId, @PathVariable("userid") Long userId){
        deskService.addUserOnDesk(deskId, userId, RightType.MEMBER);
        return ResponseEntity.ok("Пользователь успешно добавлен!");
    }

    @DeleteMapping("/{id}/users/{userid}")
    public ResponseEntity<String> deleteUserFromDesk(@PathVariable("id") Long deskId, @PathVariable("userid") Long userId){
        deskService.deleteUserFromDesk(deskId, userId);
        return ResponseEntity.ok("Пользователь успешно удален!");
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UsersOnDeskResponseDto>> getUsersOnDesk(@PathVariable("id") Long deskId){
        return ResponseEntity.ok(deskService.getUsersOnDesk(deskId));
    }
}
