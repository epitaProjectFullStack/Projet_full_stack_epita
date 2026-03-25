package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.controller.rest.AuthController;
import fr.epita.backend.converter.ControllerConverter.AuthControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// WHY: @WebMvcTest ne charge que la couche HTTP — pas de BDD ni Kafka nécessaire
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // WHY: les dépendances du controller doivent être mockées
    @MockBean
    private AuthService authService;

    @MockBean
    private AuthControllerConverter authConverter;

    @Test
    void auth_should_return_200() throws Exception {
        // WHY: on simule un login valide
        when(authService.auth("admin", "admin")).thenReturn(new UserEntity());
        when(authConverter.FromEntityToAuthResponse(any())).thenReturn(null);

        String json = """
        {
          "login": "admin",
          "password": "admin"
        }
        """;

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}