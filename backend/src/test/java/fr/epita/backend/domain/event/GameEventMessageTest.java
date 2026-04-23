package fr.epita.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * THIS TEST CLASS PROVES THAT:
 * Lombok-generated methods (getters/setters/constructors)
 * behave correctly for GameEventMessage
 *
 * WHY:
 * Even if it's a simple DTO, covering it improves global coverage
 * with almost zero effort (high ROI test)
 * ============================================================
 */
class GameEventMessageTest {

    @Test
    void constructor_should_set_all_fields() {

        // GIVEN
        GameEvent event = GameEvent.GAME_CREATED;
        UUID gameId = UUID.randomUUID();
        Instant now = Instant.now();

        // WHEN
        GameEventMessage message = new GameEventMessage(event, gameId, now);

        // THEN
        assertThat(message.getEventType()).isEqualTo(event);
        assertThat(message.getGameId()).isEqualTo(gameId);
        assertThat(message.getOccurredAt()).isEqualTo(now);
    }

    @Test
    void setters_and_getters_should_work() {

        // GIVEN
        GameEventMessage message = new GameEventMessage();

        GameEvent event = GameEvent.GAME_UPDATED;
        UUID gameId = UUID.randomUUID();
        Instant now = Instant.now();

        // WHEN
        message.setEventType(event);
        message.setGameId(gameId);
        message.setOccurredAt(now);

        // THEN
        assertThat(message.getEventType()).isEqualTo(event);
        assertThat(message.getGameId()).isEqualTo(gameId);
        assertThat(message.getOccurredAt()).isEqualTo(now);
    }
}