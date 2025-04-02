package com.project.taskipro.service;

import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.dto.mapper.user.UserMapper;
import com.project.taskipro.dto.user.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws ResponseStatusException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с именем " + username + " не найден"));
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован!");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден!"));
    }

    public UserResponseDto getUserDtoById(Long id) throws ResponseStatusException {
        return userMapper.toUserResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id" + id + " не найден")));
    }

    public void setUserFields(Long userId, UserFieldsDto request){
        User user = getUserById(userId);
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setPassword(request.password());

        userRepository.save(user);
    }

    public void deleteUserById(Long id){
        User user = getUserById(id);

        userRepository.delete(user);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id" + userId + " не найден"));
    }

}