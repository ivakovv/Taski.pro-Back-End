package com.project.taskipro.service;

import com.project.taskipro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.project.taskipro.entity.User;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }
}