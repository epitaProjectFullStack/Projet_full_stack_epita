package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.rest.UserController;
import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserControllerConverter converter;

    @Test
    void createUser_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a user with valid data returns HTTP 200
        // ============================================================

        // WHY:
        // Cas nominal → données valides

        UserEntity entity = new UserEntity();

        when(converter.fromRequestToEntity(any())).thenReturn(entity);
        when(userService.createUser(any())).thenReturn(entity);
        when(converter.fromEntityToResponse(any())).thenReturn(null);

        UserRequest request = new UserRequest();
        request.setLogin("user");
        request.setPassword("password");
        request.setMail("mail@test.com");

        // HOW:
        // - POST avec JSON valide
        // - mocks service + converter
        // - vérifie HTTP 200

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_should_return_400_if_request_null() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Sending null request returns HTTP 400
        // ============================================================

        // WHY:
        // Le controller refuse les requêtes nulles

        // HOW:
        // - body = "null"
        // - controller déclenche INVALID_REQUEST

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void getUsers_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving all users returns HTTP 200
        // ============================================================

        // WHY:
        // endpoint GET fonctionne

        when(userService.getUsers()).thenReturn(List.of(new UserEntity()));
        when(converter.fromEntitiesToUsersResponse(any())).thenReturn(null);

        // HOW:
        // - GET /api/user
        // - mock service
        // - vérifie 200

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving existing user returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(userService.getUser(id)).thenReturn(new UserEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(null);

        // HOW:
        // - GET avec id valide
        // - service mock retourne un user

        mockMvc.perform(get("/api/user/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void getUser_should_return_400_if_invalid_uuid() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Invalid UUID format returns HTTP 400
        // ============================================================

        // WHY:
        // Spring ne peut pas parser UUID

        // HOW:
        // - id invalide → conversion échoue → 400

        mockMvc.perform(get("/api/user/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating user returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(converter.fromRequestToEntity(any())).thenReturn(new UserEntity());
        when(userService.updateUser(eq(id), any())).thenReturn(new UserEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(null);

        UserRequest request = new UserRequest();
        request.setLogin("user");
        request.setPassword("password");
        request.setMail("mail@test.com");

        // HOW:
        // - PUT avec données valides
        // - service mock
        // - vérifie 200

        mockMvc.perform(put("/api/user/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_should_return_204() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting user returns HTTP 204
        // ============================================================

        UUID id = UUID.randomUUID();

        // HOW:
        // - DELETE endpoint
        // - vérifie statut 204
        // - vérifie appel service

        mockMvc.perform(delete("/api/user/" + id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(id);
    }
}