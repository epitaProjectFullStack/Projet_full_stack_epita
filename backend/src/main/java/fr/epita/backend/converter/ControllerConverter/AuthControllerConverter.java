package fr.epita.backend.converter.ControllerConverter;

import org.springframework.stereotype.Component;

import fr.epita.backend.controller.api.response.AuthResponse;
import fr.epita.backend.domain.entity.UserEntity;

@Component
public class AuthControllerConverter {

    public AuthResponse FromEntityToAuthResponse(UserEntity entity) {
        AuthResponse response = new AuthResponse();
        response.setToken(entity.getToken());
        return response;
    }
}
