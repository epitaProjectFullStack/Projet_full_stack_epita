package fr.epita.backend.controller.api.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
