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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import fr.epita.backend.utils.ErrorCode;

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

    /*SI j’envoie une requête POST /api/auth valide
ALORS l’API répond 200 OK */
    @Test
    void auth_should_return_200() throws Exception {
        // WHY: on simule un login valide
        when(authService.auth("admin", "admin")).thenReturn(new UserEntity()); //"je suppose que le service marche"
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

    @Test
    void auth_should_return_400_if_request_invalid() throws Exception {

        // WHY:
        // Vérifie que l’API refuse une requête invalide
        // (ex: login manquant)

        String json = """
        {
          "password": "admin"
        }
        """;

        // HOW:
        // On envoie une requête invalide
        // on attend une erreur HTTP 400
        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void auth_should_return_401_if_wrong_credentials() throws Exception {

        // WHY:
        // sécurité → mauvais login/password

        when(authService.auth("admin", "wrong"))
                .thenThrow(ErrorCode.BAD_CREDENTIAL.toException());

        String json = """
        {
          "login": "admin",
          "password": "wrong"
        }
        """;

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }
}