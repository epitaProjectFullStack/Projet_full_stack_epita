package fr.epita.backend.service;

import fr.epita.backend.data.model.RefreshTokenModel;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.RefreshTokenRepository;
import fr.epita.backend.domain.service.RefreshTokenService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    private final RefreshTokenRepository repository = mock(RefreshTokenRepository.class);

    private final RefreshTokenService service =
            new RefreshTokenService(repository, 100000);

    @Test
    void createRefreshToken_should_generate_and_store_token() {

        // WHY:
        // Vérifie que la création d’un refresh token persiste bien en DB

        UserModel user = new UserModel();

        String token = service.createRefreshToken(user);

        // THEN
        assertThat(token).isNotNull();
        verify(repository).save(any());
    }

    @Test
    void validateRefreshToken_should_fail_if_revoked() {

        // WHY:
        // Un token révoqué ne doit jamais être accepté

        RefreshTokenModel model = new RefreshTokenModel();
        model.setRevoked(true);

        when(repository.findByTokenHash(any()))
                .thenReturn(Optional.of(model));

        assertThatThrownBy(() -> service.validateRefreshToken("token"))
                .isInstanceOf(RuntimeException.class);
    }
}