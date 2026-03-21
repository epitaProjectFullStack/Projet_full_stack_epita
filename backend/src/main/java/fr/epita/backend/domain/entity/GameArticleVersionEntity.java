package fr.epita.backend.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GameArticleVersionEntity {
    private UUID uuid;
    private UUID gameId;
    private UUID authorId;
    private String authorLogin;
    private String articleName;
    private String articleContent;
    private Integer version;
    private Instant createdAt;
}
