package fr.epita.backend.controller.api.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GameRevertRequest {
    private UUID authorId;
}
