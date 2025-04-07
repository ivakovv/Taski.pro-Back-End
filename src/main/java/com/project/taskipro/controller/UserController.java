package com.project.taskipro.controller;

import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.codes.CodeType;
import com.project.taskipro.service.CodesService;
import com.project.taskipro.service.UserCredentialsResetService;
import com.project.taskipro.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("api/v1/profile")
public class UserController {
    private final UserServiceImpl userService;
    private final UserCredentialsResetService userCredentialsResetService;
    private final CodesService codesService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponseDto> getUserById(){
        return ResponseEntity.ok(userService.getUserDtoById());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поля пользователя умпешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping()
    public ResponseEntity<Void> updateUserFields(@RequestBody UserFieldsDto request){
        userService.setUserFields(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteUserById(@RequestBody UserFieldsDto request){

        userService.deleteUserById(request);
        return ResponseEntity.ok("Пользователь успешно удален");

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Код отправлен на почту"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> sendPasswordCode(@RequestParam String email) {
        userCredentialsResetService.sendCodePassword(email);
        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Код отправлен на почту"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/send-code-email")
    public ResponseEntity<Void> changeMail(){

        userCredentialsResetService.sendCodeMail();
        return ResponseEntity.ok().build();

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Код проверян"),
            @ApiResponse(responseCode = "404", description = "Код не найден")
    })
    @GetMapping("/is-valid-code/{code}/{type}")
    public ResponseEntity<Boolean> isValidCode(@PathVariable("code") String code,
                                               @PathVariable("type") CodeType type){

        return ResponseEntity.ok(codesService.isValidCode(code, type));
    }

}
