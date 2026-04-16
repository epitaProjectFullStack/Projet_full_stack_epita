package fr.epita.backend.controller.api.request;

import fr.epita.backend.utils.GameStatus;
import lombok.Data;

@Data
public class GameModerationRequest {
    private GameStatus status;
}
