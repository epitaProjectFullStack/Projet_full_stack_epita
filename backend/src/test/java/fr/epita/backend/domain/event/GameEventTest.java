package fr.epita.backend.domain.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * Enum values and associated integer codes are correct
 *
 * WHY:
 * Ensures mapping between business event and numeric value
 * ============================================================
 */
class GameEventTest {

    @Test
    void enum_should_have_correct_values() {

        // THEN
        assertThat(GameEvent.GAME_CREATED.getEvent()).isEqualTo(0);
        assertThat(GameEvent.GAME_UPDATED.getEvent()).isEqualTo(1);
        assertThat(GameEvent.GAME_DELETED.getEvent()).isEqualTo(2);
        assertThat(GameEvent.GAME_REVERTED.getEvent()).isEqualTo(3);
    }

    @Test
    void valueOf_should_return_correct_enum() {

        // WHEN
        GameEvent event = GameEvent.valueOf("GAME_CREATED");

        // THEN
        assertThat(event).isEqualTo(GameEvent.GAME_CREATED);
    }
}