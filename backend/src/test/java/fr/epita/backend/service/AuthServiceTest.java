package fr.epita.backend.service;

import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDataConverter converter = mock(UserDataConverter.class);

    private final AuthService authService = new AuthService(userRepository, converter);

    @Test
    void auth_should_work() {
        // WHY:
        // Cas nominal login OK

        UserModel user = new UserModel();
        user.setPassword("123");
        user.setBanned(false);

        when(userRepository.findByLogin("login"))
                .thenReturn(Optional.of(user));

        authService.auth("login", "123");

        // HOW:
        // Pas d’exception = succès
    }

    @Test
    void auth_should_fail_if_wrong_password() {
        // WHY:
        // sécurité

        UserModel user = new UserModel();
        user.setPassword("123");

        when(userRepository.findByLogin("login"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                authService.auth("login", "wrong")
        ).isInstanceOf(RuntimeException.class);
    }
}