package fr.epita.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.controller.api.request.GameRequest;
import fr.epita.backend.controller.rest.GameController;
import fr.epita.backend.converter.ControllerConverter.GameControllerConverter;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.domain.service.GameService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.UUID;

// WHY: @WebMvcTest ne charge que la couche HTTP — pas de BDD ni Kafka nécessaire
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // WHY: les dépendances du controller doivent être mockées
    @MockBean
    private GameService gameService;

    @MockBean
    private GameControllerConverter gameControllerConverter;

    @Test
    void createGame_should_return_200() throws Exception {
        // WHY: on simule la création d'un jeu sans toucher la BDD
        GameEntity fakeEntity = new GameEntity();
        when(gameControllerConverter.fromRequestToEntity(any())).thenReturn(fakeEntity);
        when(gameService.createGame(any())).thenReturn(fakeEntity);
        when(gameControllerConverter.fromEntityToResponse(any())).thenReturn(null);

        GameRequest request = new GameRequest();
        request.setAuthorId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        request.setSubjectGameName("Zelda");
        request.setArticleName("test");
        request.setArticleContent("content");

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}