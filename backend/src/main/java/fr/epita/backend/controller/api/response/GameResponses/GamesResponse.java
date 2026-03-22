package fr.epita.backend.controller.api.response.GameResponses;

import lombok.Data;

import java.util.List;

@Data
public class GamesResponse {
    private List<GameResponse> list;
}
