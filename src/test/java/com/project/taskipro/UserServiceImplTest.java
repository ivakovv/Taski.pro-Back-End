package com.project.taskipro;

import com.project.taskipro.dto.TokenResponseDto;
import com.project.taskipro.dto.UserFieldsDto;
import com.project.taskipro.dto.mapper.user.UserMapper;
import com.project.taskipro.dto.user.UserResponseDto;
import com.project.taskipro.model.user.User;
import com.project.taskipro.repository.TokenRepository;
import com.project.taskipro.repository.UserRepository;
import com.project.taskipro.service.JwtService;
import com.project.taskipro.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private final String testUsername = "testuser";
    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(testUsername);
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername(testUsername);

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            userService.loadUserByUsername(testUsername);
        });
    }

    @Test
    void existsByUsername_UserExists_ReturnsTrue() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        assertTrue(userService.existsByUsername(testUsername));
    }

    @Test
    void existsByUsername_UserNotExists_ReturnsFalse() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.empty());

        assertFalse(userService.existsByUsername(testUsername));
    }

    @Test
    void existsByEmail_UserExists_ReturnsTrue() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        assertTrue(userService.existsByEmail(testEmail));
    }

    @Test
    void existsByEmail_UserNotExists_ReturnsFalse() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertFalse(userService.existsByEmail(testEmail));
    }

    @Test
    void findByUsername_UserExists_ReturnsUser() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        User result = userService.findByUsername(testUsername);

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
    }

    @Test
    void findByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            userService.findByUsername(testUsername);
        });
    }

    @Test
    void getCurrentUser_Authenticated_ReturnsUser() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
    }

    @Test
    void getCurrentUser_NotAuthenticated_ThrowsException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            userService.getCurrentUser();
        });
    }

    @Test
    void getUserDtoById_ReturnsUserResponseDto() {
        UserResponseDto expectedDto = new UserResponseDto(testUsername, "Test", "User", testEmail);

        when(userMapper.toUserResponseDto(testUser)).thenReturn(expectedDto);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        UserResponseDto result = userService.getUserDtoById();

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    void setUserFields_UpdatesUsername_ReturnsNewTokens() {
        String newUsername = "newusername";
        UserFieldsDto request = new UserFieldsDto(
                Optional.of(newUsername),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));

        TokenResponseDto expectedTokens = new TokenResponseDto("newAccess", "newRefresh");
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("newAccess");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("newRefresh");

        TokenResponseDto result = userService.setUserFields(request);

        assertNotNull(result);
        assertEquals(expectedTokens.accessToken(), result.accessToken());
        assertEquals(expectedTokens.refreshToken(), result.refreshToken());
        verify(userRepository).save(testUser);
        assertEquals(newUsername, testUser.getUsername());
    }

    @Test
    void setUserFields_UpdatesPassword_Successfully() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(oldPassword),
                Optional.of(newPassword)
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(oldPassword, testPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        TokenResponseDto result = userService.setUserFields(request);

        assertNull(result);
        verify(userRepository).save(testUser);
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    void setUserFields_InvalidPasswordChange_ThrowsException() {
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of("wrongPassword"),
                Optional.of("newPassword")
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", testPassword)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            userService.setUserFields(request);
        });
    }

    @Test
    void updatePasswordWithoutAuth_ValidRequest_UpdatesPassword() {
        String newPassword = "newPassword";
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(testEmail),
                Optional.empty(),
                Optional.of(newPassword)
        );

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.updatePasswordWithoutAuth(request);

        verify(userRepository).save(testUser);
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    void updatePasswordWithoutAuth_MissingEmail_ThrowsException() {
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of("newPassword")
        );

        assertThrows(ResponseStatusException.class, () -> {
            userService.updatePasswordWithoutAuth(request);
        });
    }

    @Test
    void deleteUserById_ValidPassword_DeletesUser() {
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(testPassword),
                Optional.empty()
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, testPassword)).thenReturn(true);

        userService.deleteUserById(request);

        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUserById_InvalidPassword_ThrowsException() {
        UserFieldsDto request = new UserFieldsDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of("wrongPassword"),
                Optional.empty()
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", testPassword)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUserById(request);
        });
    }

    @Test
    void getUserById_UserExists_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void getUserById_UserNotExists_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            userService.getUserById(1L);
        });
    }

    @Test
    void getUserByMail_UserExists_ReturnsUser() {
        when(userRepository.getUserByEmail(testEmail)).thenReturn(Optional.of(testUser));

        User result = userService.getUserByMail(testEmail);

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void getUserByMail_UserNotExists_ThrowsException() {
        when(userRepository.getUserByEmail(testEmail)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            userService.getUserByMail(testEmail);
        });
    }
}
