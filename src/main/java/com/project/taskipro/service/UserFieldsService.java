package com.project.taskipro.service;


import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserFieldsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void setUserFields(UserFieldsDto request){
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        user.setLastname(request.lastname());
        user.setFirstname(request.firstname());
        user.setPassword(passwordEncoder.encode(request.password()));

    }

}
