
package fr.epita.backend.controller.api.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
