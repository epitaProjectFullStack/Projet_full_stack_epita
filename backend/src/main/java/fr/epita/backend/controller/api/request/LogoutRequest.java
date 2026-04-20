package fr.epita.backend.controller.api.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
