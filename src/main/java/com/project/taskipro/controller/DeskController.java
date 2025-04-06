package com.project.taskipro.controller;

import com.project.taskipro.dto.desk.AddUserOnDeskDto;
import com.project.taskipro.dto.desk.ChangeUserRightsDto;
import com.project.taskipro.dto.desk.DeskCreateDto;
import com.project.taskipro.dto.desk.DeskResponseDto;
import com.project.taskipro.dto.desk.DeskUpdateDto;
import com.project.taskipro.dto.desk.UsersOnDeskResponseDto;
import com.project.taskipro.dto.mapper.desk.MapperToDeskResponseDto;
import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.desks.Desks;
import com.project.taskipro.service.DeskService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/desk")
public class DeskController {
    private final DeskService deskService;
    private final MapperToDeskResponseDto mapperToDeskResponseDto;

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доска успешно создана!")
    })
    public ResponseEntity<DeskResponseDto> createDesk(@RequestBody DeskCreateDto deskCreateDto){
        return ResponseEntity.ok(deskService.createDesk(deskCreateDto));
    }
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доска успешно удалена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления доски"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена")
    })
    public ResponseEntity<Void> deleteDesk(@PathVariable Long id){
        deskService.deleteDesk(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доска успешно обновлена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления доски"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена")
    })
    public ResponseEntity<DeskResponseDto> updateDesk(@PathVariable Long id, @RequestBody DeskUpdateDto deskUpdateDto){
        return ResponseEntity.ok(deskService.updateDesk(id, deskUpdateDto));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доска успешно получена"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена")
    })
    public ResponseEntity<DeskResponseDto> getDeskById(@PathVariable Long id){
        return ResponseEntity.ok(deskService.getDesk(id));
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список досок получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<DeskResponseDto>> getDesksForUser(){
        List<DeskResponseDto> desks = deskService.getDesksForUser();
        return ResponseEntity.ok(desks);
    }

    @PostMapping("/{id}/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно добавлен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для добавления пользователя на доску"),
            @ApiResponse(responseCode = "404", description = "Пользователь или доска не найдены"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже есть на этой доске")
    })
    public ResponseEntity<Void> addUserOnDesk(@PathVariable("id") Long deskId, @RequestBody AddUserOnDeskDto addUserDto){
        deskService.addUserOnDesk(deskId, addUserDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не атворизован")
    })
    public ResponseEntity<List<UserResponseDto>> getListOfUsers(){
        return ResponseEntity.ok(deskService.getListOfUsers());
    }
    
    @PutMapping("/{id}/users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Права успешно изменены!"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав на изменение прав пользователя!"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден!")
    })
    public ResponseEntity<Void> changeUserRightsOnDesk(@PathVariable("id") Long deskId, @RequestBody ChangeUserRightsDto changeUserRightsDto){
        deskService.changeUserRightsOnDesk(deskId, changeUserRightsDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/users/{userid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления пользователя с доски"),
            @ApiResponse(responseCode = "404", description = "Пользователь или доска не найдены")
    })
    public ResponseEntity<Void> deleteUserFromDesk(@PathVariable("id") Long deskId, @PathVariable("userid") Long userId){
        deskService.deleteUserFromDesk(deskId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для получения пользователей"),
            @ApiResponse(responseCode = "404", description = "Доска не найдена")
    })
    public ResponseEntity<List<UsersOnDeskResponseDto>> getUsersOnDesk(@PathVariable("id") Long deskId){
        return ResponseEntity.ok(deskService.getUsersOnDesk(deskId));
    }
}
