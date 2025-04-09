package com.project.taskipro.service;

import com.project.taskipro.dto.TokenResponseDto;
import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.dto.mapper.user.UserMapper;
import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.auth.Token;
import com.project.taskipro.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws ResponseStatusException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с именем %s не найден", username)));
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
    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с именем %s не найден", username)));
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

    public UserResponseDto getUserDtoById() throws ResponseStatusException {
        return userMapper.toUserResponseDto(getCurrentUser());
    }

    public TokenResponseDto setUserFields(UserFieldsDto request) {
        User user = getCurrentUser();
        String oldUsername = user.getUsername();

        request.username().ifPresent(user::setUsername);
        request.firstname().ifPresent(user::setFirstname);
        request.lastname().ifPresent(user::setLastname);
        request.email().ifPresent(user::setEmail);

        if (request.oldPassword().isPresent() || request.newPassword().isPresent()) {
            if (request.oldPassword().isEmpty() || request.newPassword().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Для смены пароля нужно указать и старый, и новый пароль");
            }

            String oldPassword = request.oldPassword().get();
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный текущий пароль");
            }

            String newPassword = request.newPassword().get();
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);

        if (request.username().isPresent() && !request.username().get().equals(oldUsername)) {
            return updateTokensForUser(user);
        }

        return null;
    }

    private TokenResponseDto updateTokensForUser(User user) {
        List<Token> activeTokens = tokenRepository.findAllAccessTokenByUser(user.getId());

        activeTokens.forEach(token -> token.setLoggedOut(true));
        tokenRepository.saveAll(activeTokens);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        Token newToken = new Token();
        newToken.setAccessToken(newAccessToken);
        newToken.setRefreshToken(newRefreshToken);
        newToken.setUser(user);
        newToken.setLoggedOut(false);
        tokenRepository.save(newToken);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    public void updatePasswordWithoutAuth(UserFieldsDto request) {
        String email = request.email()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email обязателен для смены пароля"));

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Пользователь с указанным email не найден"));

        String newPassword = request.newPassword()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Новый пароль обязателен"));

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }
    public void deleteUserById(UserFieldsDto request){
        User user = getCurrentUser();

        String oldPassword = request.oldPassword().get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный текущий пароль");
        }
        userRepository.delete(user);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с id %d не найден", userId)));
    }

    public User getUserByMail(String email){
        return userRepository.getUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Пользователь с email %s не найден", email)));
    }

}