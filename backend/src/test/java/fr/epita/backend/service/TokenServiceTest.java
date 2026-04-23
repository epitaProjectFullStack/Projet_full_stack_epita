package fr.epita.backend.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.domain.service.TokenService;
import fr.epita.backend.utils.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TokenServiceTest {

    @Test
    void generateToken_should_return_valid_jwt() {

        // WHY:
        // Vérifie que le JWT est bien généré

        TokenService service =
                new TokenService("my-super-secret-key-that-is-very-long-123456", 100000);

        UserModel user = new UserModel();
        user.setUuid(UUID.randomUUID());
        user.setLogin("user");
        user.setRole(Role.USER);

        String token = service.generateToken(user);

        assertThat(token).isNotBlank();
    }
}