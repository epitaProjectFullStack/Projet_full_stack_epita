package fr.epita.backend.controller.api.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GameRequest {
    private UUID authorId;
    private String subjectGameName;
    private String articleName;
    private String articleContent;
}
