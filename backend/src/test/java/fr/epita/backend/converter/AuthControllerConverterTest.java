package fr.epita.backend.converter;

import fr.epita.backend.converter.ControllerConverter.AuthControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.controller.api.response.AuthResponse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AuthControllerConverterTest {

    private final AuthControllerConverter converter = new AuthControllerConverter();

    @Test
    void fromEntityToAuthResponse_should_map_token() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // User token is correctly mapped to response
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setToken("abc");

        AuthResponse response = converter.FromEntityToAuthResponse(entity);

        assertThat(response.getToken()).isEqualTo("abc");
    }
}