package fr.epita.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEventMessage {

    private GameEvent eventType;
    private UUID gameId;
    private Instant occurredAt;

}
