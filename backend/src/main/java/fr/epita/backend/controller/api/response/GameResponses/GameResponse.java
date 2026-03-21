package fr.epita.backend.controller.api.response.GameResponses;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GameResponse {
    private UUID uuid;
    private UUID authorId;
    private String authorLogin;
    private String subjectGameName;
    private String articleName;
    private String articleContent;
    private Integer version;
    private Instant createdAt;
}
