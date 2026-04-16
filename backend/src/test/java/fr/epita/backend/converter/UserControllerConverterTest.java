package fr.epita.backend.converter;

import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.controller.api.request.AdminUserRequest;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.controller.api.request.UserRequest;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserControllerConverterTest {

    private final UserControllerConverter converter = new UserControllerConverter();

    @Test
    void fromEntityToResponse_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Entity is correctly mapped to user response
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setLogin("alice");

        assertThat(converter.fromEntityToResponse(entity).getLogin())
                .isEqualTo("alice");
    }

    @Test
    void fromEntityToAdminResponse_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Admin response contains extended fields
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setPassword("pwd");

        assertThat(converter.fromEntityToAdminResponse(entity).getPassword())
                .isEqualTo("pwd");
    }

    @Test
    void fromEntitiesToUsersResponse_should_map_list() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // List conversion works
        // ============================================================

        UserEntity entity = new UserEntity();

        assertThat(converter.fromEntitiesToUsersResponse(List.of(entity)).getList())
                .hasSize(1);
    }

    @Test
    void fromRequestToEntity_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Request is converted into entity
        // ============================================================

        UserRequest request = new UserRequest();
        request.setLogin("alice");

        assertThat(converter.fromRequestToEntity(request).getLogin())
                .isEqualTo("alice");
    }

    @Test
    void fromAdminRequestToEntity_should_map_admin_fields() {
        AdminUserRequest request = new AdminUserRequest();
        request.setLogin("alice");
        request.setBanned(true);

        UserEntity entity = converter.fromAdminRequestToEntity(request);

        assertThat(entity.getLogin()).isEqualTo("alice");
        assertThat(entity.isBanned()).isTrue();
    }
}
