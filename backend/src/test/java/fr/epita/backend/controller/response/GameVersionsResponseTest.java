package fr.epita.backend.controller.response;

import org.junit.jupiter.api.Test;

import fr.epita.backend.controller.api.response.GameResponses.GameVersionResponse;
import fr.epita.backend.controller.api.response.GameResponses.GameVersionsResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * GameVersionsResponse wraps version list correctly
 * ============================================================
 */
class GameVersionsResponseTest {

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        GameVersionsResponse response = new GameVersionsResponse();
        GameVersionResponse version = new GameVersionResponse();

        // WHEN
        response.setList(List.of(version));

        // THEN
        assertThat(response.getList()).hasSize(1);
        assertThat(response.getList().get(0)).isEqualTo(version);
    }
}