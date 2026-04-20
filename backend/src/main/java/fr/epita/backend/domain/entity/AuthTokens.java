package fr.epita.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokens {
    private final String accessToken;
    private final String refreshToken;
}
