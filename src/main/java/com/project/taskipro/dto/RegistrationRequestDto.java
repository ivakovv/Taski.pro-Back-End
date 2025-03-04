package com.project.taskipro.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
