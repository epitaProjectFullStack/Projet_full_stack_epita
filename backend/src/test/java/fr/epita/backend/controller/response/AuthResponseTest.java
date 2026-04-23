package fr.epita.backend.controller.response;

import org.junit.jupiter.api.Test;

import fr.epita.backend.controller.api.response.AuthResponse;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * Lombok-generated getters/setters work correctly
 *
 * WHY:
 * Simple DTO → fast coverage gain with no business logic
 * ============================================================
 */
class AuthResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        AuthResponse response = new AuthResponse();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        // WHEN
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        // THEN
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    }
}