package fr.epita.backend.controller.response;

import org.junit.jupiter.api.Test;

import fr.epita.backend.controller.api.response.GameResponses.GameVersionResponse;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * GameVersionResponse correctly stores version data
 * ============================================================
 */
class GameVersionResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        GameVersionResponse response = new GameVersionResponse();

        UUID uuid = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Instant now = Instant.now();

        // WHEN
        response.setUuid(uuid);
        response.setGameId(gameId);
        response.setAuthorId(authorId);
        response.setAuthorLogin("login");
        response.setArticleName("name");
        response.setArticleContent("content");
        response.setVersion(1);
        response.setCreatedAt(now);

        // THEN
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getGameId()).isEqualTo(gameId);
        assertThat(response.getAuthorId()).isEqualTo(authorId);
        assertThat(response.getAuthorLogin()).isEqualTo("login");
        assertThat(response.getArticleName()).isEqualTo("name");
        assertThat(response.getArticleContent()).isEqualTo("content");
        assertThat(response.getVersion()).isEqualTo(1);
        assertThat(response.getCreatedAt()).isEqualTo(now);
    }
}