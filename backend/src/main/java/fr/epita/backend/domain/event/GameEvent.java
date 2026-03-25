package fr.epita.backend.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameEvent {
    GAME_CREATED(0),
    GAME_UPDATED(1),
    GAME_DELETED(2),
    GAME_REVERTED(3);

    public Integer event;
}
