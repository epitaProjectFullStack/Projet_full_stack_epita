package fr.epita.backend.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GameEntity {
    private UUID uuid;
    private UUID authorId;
    private String authorLogin;
    private String subjectGameName;
    private String articleName;
    private String articleContent;
    private Integer version;
    private Instant createdAt;
}
