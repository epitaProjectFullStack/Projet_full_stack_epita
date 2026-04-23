package fr.epita.backend.controller.response;

import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
import fr.epita.backend.utils.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * UserResponse stores user data correctly
 * ============================================================
 */
class UserResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        UserResponse response = new UserResponse();
        UUID id = UUID.randomUUID();

        // WHEN
        response.setId(id);
        response.setLogin("user");
        response.setMail("mail@test.com");
        response.setRole(Role.USER);

        // THEN
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getLogin()).isEqualTo("user");
        assertThat(response.getMail()).isEqualTo("mail@test.com");
        assertThat(response.getRole()).isEqualTo(Role.USER);
    }
}