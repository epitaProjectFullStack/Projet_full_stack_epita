package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.AuthTokens;
import fr.epita.backend.controller.rest.AuthController;
import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.domain.service.TokenService;
import fr.epita.backend.utils.ErrorCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // WHY:
    // On mock les dépendances → on teste uniquement le controller
    @MockBean
    private AuthService authService;

    @MockBean
    private UserControllerConverter userControllerConverter;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    void auth_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // A valid authentication request returns HTTP 200
        // ============================================================

        // WHY:
        // Cas nominal → login/password corrects

        when(authService.auth("admin", "admin"))
                .thenReturn(new AuthTokens("jwt-access-token", "jwt-refresh-token"));

        String json = """
        {
          "login": "admin",
          "password": "admin"
        }
        """;

        // HOW:
        // - Envoi d’un POST avec JSON valide
        // - Le service mock retourne un JWT
        // - Le controller renvoie 200 OK

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("jwt-refresh-token"));
    }

    @Test
    void auth_should_return_400_if_request_invalid() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Missing required fields returns HTTP 400
        // ============================================================

        // WHY:
        // Le controller valide que login/password ne sont pas null

        String json = """
        {
          "password": "admin"
        }
        """;

        // HOW:
        // - On envoie un JSON incomplet (login manquant)
        // - Le controller déclenche INVALID_REQUEST
        // - Spring transforme en HTTP 400

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        // On vérifie que le service n’est JAMAIS appelé
        verifyNoInteractions(authService);
    }

    @Test
    void auth_should_return_400_if_request_null() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Sending null body returns HTTP 400
        // ============================================================

        // WHY:
        // Protection contre body null

        // HOW:
        // - On envoie "null"
        // - Spring + controller déclenchent une erreur
        // - HTTP 400 attendu

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void auth_should_return_401_if_wrong_credentials() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Wrong credentials return HTTP 401
        // ============================================================

        // WHY:
        // Sécurité → login/password incorrect

        when(authService.auth("admin", "wrong"))
                .thenThrow(ErrorCode.BAD_CREDENTIAL.toException());

        String json = """
        {
          "login": "admin",
          "password": "wrong"
        }
        """;

        // HOW:
        // - Le service mock lance BAD_CREDENTIAL
        // - Spring convertit en 401

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void auth_should_return_403_if_user_banned() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Banned user returns HTTP 403
        // ============================================================

        // WHY:
        // Règle métier → utilisateur banni

        when(authService.auth("admin", "admin"))
                .thenThrow(ErrorCode.BANNED_USER.toException());

        String json = """
        {
          "login": "admin",
          "password": "admin"
        }
        """;

        // HOW:
        // - Le service lance BANNED_USER
        // - Spring transforme en 403

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void refresh_should_return_200() throws Exception {
        when(authService.refresh("refresh-token"))
                .thenReturn(new AuthTokens("new-access-token", "new-refresh-token"));

        String json = """
        {
          "refreshToken": "refresh-token"
        }
        """;

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    void refresh_should_return_401_if_refresh_token_invalid() throws Exception {
        when(authService.refresh("refresh-token"))
                .thenThrow(ErrorCode.INVALID_REFRESH_TOKEN.toException());

        String json = """
        {
          "refreshToken": "refresh-token"
        }
        """;

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_should_return_204() throws Exception {
        String json = """
        {
          "refreshToken": "refresh-token"
        }
        """;

        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(authService).logout("refresh-token");
    }
}
