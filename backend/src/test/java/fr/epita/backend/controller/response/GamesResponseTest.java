package fr.epita.backend.controller.response;


import org.junit.jupiter.api.Test;

import fr.epita.backend.controller.api.response.GameResponses.GameResponse;
import fr.epita.backend.controller.api.response.GameResponses.GamesResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * GamesResponse correctly wraps a list of GameResponse
 * ============================================================
 */
class GamesResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        GamesResponse response = new GamesResponse();
        GameResponse game = new GameResponse();

        // WHEN
        response.setList(List.of(game));

        // THEN
        assertThat(response.getList()).hasSize(1);
        assertThat(response.getList().get(0)).isEqualTo(game);
    }
}