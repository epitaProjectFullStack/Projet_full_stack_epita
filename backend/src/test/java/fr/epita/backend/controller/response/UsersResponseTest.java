package fr.epita.backend.controller.response;

import org.junit.jupiter.api.Test;

import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
import fr.epita.backend.controller.api.response.UserResponses.UsersResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * UsersResponse wraps user list correctly
 * ============================================================
 */
class UsersResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        UsersResponse response = new UsersResponse();
        UserResponse user = new UserResponse();

        // WHEN
        response.setList(List.of(user));

        // THEN
        assertThat(response.getList()).hasSize(1);
        assertThat(response.getList().get(0)).isEqualTo(user);
    }
}