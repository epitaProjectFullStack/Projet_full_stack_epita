package fr.epita.backend.converter;

import fr.epita.backend.controller.api.request.GameRequest;
import fr.epita.backend.controller.api.response.GameResponses.*;
import fr.epita.backend.converter.ControllerConverter.GameControllerConverter;
import fr.epita.backend.domain.entity.*;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class GameControllerConverterTest {

    private final GameControllerConverter converter = new GameControllerConverter();

    @Test
    void fromRequestToEntity_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Request is correctly converted to entity
        // ============================================================

        GameRequest request = new GameRequest();
        request.setAuthorId(UUID.randomUUID());
        request.setSubjectGameName("Zelda");
        request.setArticleName("article");
        request.setArticleContent("content");

        // HOW
        GameEntity entity = converter.fromRequestToEntity(request);

        assertThat(entity.getAuthorId()).isEqualTo(request.getAuthorId());
        assertThat(entity.getSubjectGameName()).isEqualTo("Zelda");
    }

    @Test
    void fromEntityToResponse_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Entity is correctly converted to response
        // ============================================================

        GameEntity entity = new GameEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setSubjectGameName("Zelda");
        entity.setVersion(1);
        entity.setCreatedAt(Instant.now());

        GameResponse response = converter.fromEntityToResponse(entity);

        assertThat(response.getUuid()).isEqualTo(entity.getUuid());
        assertThat(response.getVersion()).isEqualTo(1);
    }

    @Test
    void fromEntitiesToResponses_should_map_list() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // List conversion works correctly
        // ============================================================

        GameEntity entity = new GameEntity();

        GamesResponse response = converter.fromEntitiesToResponses(List.of(entity));

        assertThat(response.getList()).hasSize(1);
    }

    @Test
    void fromVersionEntityToResponse_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Version entity is correctly converted
        // ============================================================

        GameArticleVersionEntity entity = new GameArticleVersionEntity();
        entity.setVersion(2);

        GameVersionResponse response = converter.fromVersionEntityToResponse(entity);

        assertThat(response.getVersion()).isEqualTo(2);
    }

    @Test
    void fromVersionEntitiesToResponses_should_map_list() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Version list conversion works
        // ============================================================

        GameArticleVersionEntity entity = new GameArticleVersionEntity();

        GameVersionsResponse response =
                converter.fromVersionEntitiesToResponses(List.of(entity));

        assertThat(response.getList()).hasSize(1);
    }
}