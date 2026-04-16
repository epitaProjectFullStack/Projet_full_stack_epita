package fr.epita.backend.service;

import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.domain.service.TokenService;
import fr.epita.backend.domain.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDataConverter converter = mock(UserDataConverter.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final UserService userService = mock(UserService.class);

    private final AuthService authService = new AuthService(userRepository, converter, passwordEncoder, tokenService,
            userService);

    @Test
    void auth_should_work() {
        UserModel user = new UserModel();
        user.setPassword("hashed-password");
        user.setBanned(false);

        when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "hashed-password")).thenReturn(true);
        when(tokenService.generateToken()).thenReturn("generated-token");

        authService.auth("login", "123");

        verify(userRepository).save(user);
        assertThat(user.getToken()).isEqualTo("generated-token");
    }

    @Test
    void auth_should_fail_if_wrong_password() {
        UserModel user = new UserModel();
        user.setPassword("hashed-password");
        user.setBanned(false);

        when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.auth("login", "wrong"))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any());
    }
}
