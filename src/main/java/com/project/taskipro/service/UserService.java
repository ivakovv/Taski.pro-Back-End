package com.project.taskipro.service;

import com.project.taskipro.dto.TokenResponseDto;
import com.project.taskipro.dto.UserFieldsDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    TokenResponseDto setUserFields(UserFieldsDto request);


}