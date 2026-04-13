package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.rest.AdminUserController;
import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserControllerConverter converter;

    @MockBean
    private UserRepository userRepository;

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
        request.setLogin("admin");
        request.setPassword("pwd");
        request.setMail("admin@mail.com");

        // HOW:
        // On envoie une requête POST valide simulée

        mockMvc.perform(post("/api/admin/user")
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
        // Validation du controller

        // HOW:
        // "null" → déclenche INVALID_REQUEST

        mockMvc.perform(post("/api/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))

                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsers_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Getting all users returns HTTP 200
        // ============================================================

        // WHY:
        // endpoint GET fonctionne

        when(userService.getUsers()).thenReturn(List.of(new UserEntity()));
        when(converter.fromEntitiesToAdminUsersResponse(any())).thenReturn(null);

        // HOW:
        // appel GET simple

        mockMvc.perform(get("/api/admin/user"))

                .andExpect(status().isOk());
    }

    @Test
    void getUser_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Getting a specific user returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(userService.getUser(id)).thenReturn(new UserEntity());
        when(converter.fromEntityToAdminResponse(any())).thenReturn(null);

        // HOW:
        // GET avec ID valide

        mockMvc.perform(get("/api/admin/user/" + id))

                .andExpect(status().isOk());
    }

    @Test
    void updateUser_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating a user returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(converter.fromRequestToEntity(any())).thenReturn(new UserEntity());
        when(userService.updateUser(eq(id), any())).thenReturn(new UserEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(null);

        UserRequest request = new UserRequest();
        request.setLogin("updated");
        request.setPassword("pwd");
        request.setMail("updated@mail.com");

        // HOW:
        // PUT avec données valides

        mockMvc.perform(put("/api/admin/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk());
    }

    @Test
    void updateUser_should_return_400_if_request_null() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating with null request returns HTTP 400
        // ============================================================

        UUID id = UUID.randomUUID();

        // HOW:
        // null → validation controller

        mockMvc.perform(put("/api/admin/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))

                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_should_return_204() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting a user returns HTTP 204
        // ============================================================

        UUID id = UUID.randomUUID();

        // HOW:
        // appel DELETE

        mockMvc.perform(delete("/api/admin/user/" + id))

                .andExpect(status().isNoContent());

        verify(userService).deleteUser(id);
    }
}
