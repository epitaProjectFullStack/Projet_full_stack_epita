package fr.epita.backend.controller.api.response.GameResponses;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GameVersionResponse {
    private UUID uuid;
    private UUID gameId;
    private UUID authorId;
    private String authorLogin;
    private String articleName;
    private String articleContent;
    private Integer version;
    private Instant createdAt;
}
