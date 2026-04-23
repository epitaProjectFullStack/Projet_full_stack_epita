package fr.epita.backend.controller.response;

import fr.epita.backend.controller.api.response.AdminResponses.UserResponses.AdminUserResponse;
import fr.epita.backend.utils.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * Lombok-generated methods (equals, hashCode, toString)
 * behave correctly
 *
 * WHY:
 * JaCoCo requires explicit execution of these methods
 * to count coverage
 * ============================================================
 */
class AdminUserResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        AdminUserResponse user = new AdminUserResponse();
        UUID id = UUID.randomUUID();

        // WHEN
        user.setId(id);
        user.setLogin("admin");
        user.setPassword("pwd");
        user.setMail("mail@test.com");
        user.setRole(Role.ADMINISTRATOR);
        user.setBanned(true);

        // THEN
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getLogin()).isEqualTo("admin");
        assertThat(user.getPassword()).isEqualTo("pwd");
        assertThat(user.getMail()).isEqualTo("mail@test.com");
        assertThat(user.getRole()).isEqualTo(Role.ADMINISTRATOR);
        assertThat(user.isBanned()).isTrue();
    }

    @Test
    void equals_and_hashcode_should_work() {

        // GIVEN
        UUID id = UUID.randomUUID();

        AdminUserResponse user1 = new AdminUserResponse();
        user1.setId(id);
        user1.setLogin("admin");

        AdminUserResponse user2 = new AdminUserResponse();
        user2.setId(id);
        user2.setLogin("admin");

        // THEN
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void toString_should_not_be_null() {

        // GIVEN
        AdminUserResponse user = new AdminUserResponse();
        user.setLogin("admin");

        // WHEN
        String result = user.toString();

        // THEN
        assertThat(result).isNotNull();
        assertThat(result).contains("admin");
    }
}