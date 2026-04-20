package fr.epita.backend.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.AuthTokens;
import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.domain.service.RefreshTokenService;
import fr.epita.backend.domain.service.TokenService;
import fr.epita.backend.domain.service.UserService;
import fr.epita.backend.utils.ErrorCode;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);
    private final UserService userService = mock(UserService.class);

    private final AuthService authService = new AuthService(
            userRepository,
            passwordEncoder,
            tokenService,
            refreshTokenService,
            userService);

    @Test
    void auth_should_work() {
        UserModel user = new UserModel();
        user.setPassword("hashed-password");
        user.setBanned(false);

        when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "hashed-password")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("generated-access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn("generated-refresh-token");

        AuthTokens tokens = authService.auth("login", "123");

        assertThat(tokens.getAccessToken()).isEqualTo("generated-access-token");
        assertThat(tokens.getRefreshToken()).isEqualTo("generated-refresh-token");
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

        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void refresh_should_rotate_tokens() {
        UserModel user = new UserModel();
        user.setBanned(false);

        when(refreshTokenService.validateRefreshToken("refresh-token")).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("new-access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn("new-refresh-token");

        AuthTokens tokens = authService.refresh("refresh-token");

        assertThat(tokens.getAccessToken()).isEqualTo("new-access-token");
        assertThat(tokens.getRefreshToken()).isEqualTo("new-refresh-token");
        verify(refreshTokenService).revokeRefreshToken("refresh-token");
    }

    @Test
    void refresh_should_fail_if_user_banned() {
        UserModel user = new UserModel();
        user.setBanned(true);

        when(refreshTokenService.validateRefreshToken("refresh-token")).thenReturn(user);

        assertThatThrownBy(() -> authService.refresh("refresh-token"))
                .isInstanceOfSatisfying(RuntimeException.class, exception ->
                        assertThat(exception.getMessage()).contains(ErrorCode.BANNED_USER.getMessage()));

        verify(refreshTokenService, never()).revokeRefreshToken(any());
    }

    @Test
    void logout_should_revoke_refresh_token() {
        authService.logout("refresh-token");

        verify(refreshTokenService).revokeRefreshToken("refresh-token");
    }
}
