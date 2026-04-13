package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.controller.api.request.GameRequest;
import fr.epita.backend.controller.api.request.GameRevertRequest;
import fr.epita.backend.controller.api.response.GameResponses.GameResponse;
import fr.epita.backend.controller.api.response.GameResponses.GamesResponse;
import fr.epita.backend.controller.rest.GameController;
import fr.epita.backend.converter.ControllerConverter.GameControllerConverter;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.domain.service.GameService;
import fr.epita.backend.utils.ErrorCode;

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

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameControllerConverter converter;

    @Test
    void createGame_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // A valid request to create a game returns HTTP 200
        // ============================================================

        // WHY:
        // Cas nominal → si toutes les données sont valides,
        // le controller doit appeler le service et retourner un succès

        GameEntity entity = new GameEntity();

        when(converter.fromRequestToEntity(any())).thenReturn(entity);
        when(gameService.createGame(any())).thenReturn(entity);
        when(converter.fromEntityToResponse(any())).thenReturn(new GameResponse());

        GameRequest request = new GameRequest();
        request.setAuthorId(UUID.randomUUID());
        request.setSubjectGameName("Zelda");
        request.setArticleName("test");
        request.setArticleContent("content");

        // HOW:
        // - ObjectMapper transforme l'objet Java en JSON
        // - MockMvc simule une requête HTTP POST réelle
        // - les mocks interceptent les appels service/converter
        // - on vérifie que le controller répond HTTP 200

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // WHY:
        // Vérifie que le service est bien appelé
        verify(gameService).createGame(any());
    }

    @Test
    void createGame_should_return_400_if_request_null() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Sending a null request returns HTTP 400
        // ============================================================

        // WHY:
        // Le controller protège contre les requêtes invalides

        // HOW:
        // - On envoie "null" comme body JSON
        // - Spring transforme en request = null
        // - le controller déclenche INVALID_REQUEST → 400

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        // WHY:
        // Le service ne doit jamais être appelé
        verifyNoInteractions(gameService);
    }

    @Test
    void getGames_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving all games returns HTTP 200
        // ============================================================

        // WHY:
        // Endpoint principal → récupération de la liste

        when(gameService.getGames()).thenReturn(List.of(new GameEntity()));
        when(converter.fromEntitiesToResponses(any())).thenReturn(new GamesResponse());

        // HOW:
        // - GET /api/games simulé
        // - service mocké renvoie une liste
        // - converter transforme la réponse
        // - on vérifie HTTP 200

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk());

        verify(gameService).getGames();
    }

    @Test
    void getGame_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving an existing game returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(gameService.getGame(id)).thenReturn(new GameEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(new GameResponse());

        // HOW:
        // - GET avec ID valide
        // - service renvoie un objet
        // - controller retourne 200

        mockMvc.perform(get("/api/games/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void getGame_should_return_404_if_not_found() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Requesting a non-existing game returns HTTP 404
        // ============================================================

        UUID id = UUID.randomUUID();

        when(gameService.getGame(id))
                .thenThrow(ErrorCode.GAME_NOT_FOUND.toException());

        // HOW:
        // - le service lance une exception métier
        // - Spring la convertit en 404 automatiquement

        mockMvc.perform(get("/api/games/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGame_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating a game with valid data returns HTTP 200
        // ============================================================

        UUID id = UUID.randomUUID();

        when(converter.fromRequestToEntity(any())).thenReturn(new GameEntity());
        when(gameService.updateGame(eq(id), any())).thenReturn(new GameEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(new GameResponse());

        GameRequest request = new GameRequest();
        request.setAuthorId(UUID.randomUUID());
        request.setSubjectGameName("Zelda");
        request.setArticleName("test");
        request.setArticleContent("content");

        // HOW:
        // - PUT request simulée
        // - conversion JSON → entity
        // - service mocké
        // - réponse HTTP 200

        mockMvc.perform(put("/api/games/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGame_should_return_204() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting a game returns HTTP 204
        // ============================================================

        UUID id = UUID.randomUUID();

        // HOW:
        // - DELETE request simulée
        // - controller appelle service
        // - HTTP 204 attendu

        mockMvc.perform(delete("/api/games/" + id))
                .andExpect(status().isNoContent());

        verify(gameService).deleteGame(id);
    }

    @Test
    void revertGame_should_return_200() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Reverting a game version returns HTTP 200
        // ============================================================

        UUID gameId = UUID.randomUUID();
        UUID versionId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        when(gameService.revertGame(eq(gameId), eq(versionId), eq(authorId)))
                .thenReturn(new GameEntity());
        when(converter.fromEntityToResponse(any())).thenReturn(new GameResponse());

        GameRevertRequest request = new GameRevertRequest();
        request.setAuthorId(authorId);

        // HOW:
        // - POST avec paramètres + body
        // - service mocké
        // - réponse HTTP 200

        mockMvc.perform(post("/api/games/" + gameId + "/revert/" + versionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void revertGame_should_return_400_if_invalid() throws Exception {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Missing authorId returns HTTP 400
        // ============================================================

        UUID gameId = UUID.randomUUID();
        UUID versionId = UUID.randomUUID();

        // HOW:
        // - body JSON vide → authorId null
        // - controller déclenche INVALID_REQUEST
        // - HTTP 400 attendu

        mockMvc.perform(post("/api/games/" + gameId + "/revert/" + versionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}