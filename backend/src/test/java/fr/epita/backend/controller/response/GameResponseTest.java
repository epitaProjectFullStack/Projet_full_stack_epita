package fr.epita.backend.controller.response;

import fr.epita.backend.controller.api.response.GameResponses.GameResponse;
import fr.epita.backend.utils.GameStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * GameResponse DTO correctly stores and returns values
 *
 * WHY:
 * DTO coverage → fast and reliable gain
 * ============================================================
 */
class GameResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        GameResponse response = new GameResponse();

        UUID uuid = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Instant now = Instant.now();

        // WHEN
        response.setUuid(uuid);
        response.setAuthorId(authorId);
        response.setAuthorLogin("login");
        response.setSubjectGameName("Zelda");
        response.setArticleName("Article");
        response.setArticleContent("Content");
        response.setVersion(1);
        response.setCreatedAt(now);
        response.setStatus(GameStatus.OK);

        // THEN
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getAuthorId()).isEqualTo(authorId);
        assertThat(response.getAuthorLogin()).isEqualTo("login");
        assertThat(response.getSubjectGameName()).isEqualTo("Zelda");
        assertThat(response.getArticleName()).isEqualTo("Article");
        assertThat(response.getArticleContent()).isEqualTo("Content");
        assertThat(response.getVersion()).isEqualTo(1);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getStatus()).isEqualTo(GameStatus.OK);
    }
}