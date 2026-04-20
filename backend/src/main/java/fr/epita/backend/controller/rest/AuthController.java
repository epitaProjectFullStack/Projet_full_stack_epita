package fr.epita.backend.controller.rest;

import fr.epita.backend.controller.api.request.AuthRequest;
import fr.epita.backend.controller.api.request.LogoutRequest;
import fr.epita.backend.controller.api.request.RefreshTokenRequest;
import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.AuthResponse;
import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.domain.entity.AuthTokens;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.utils.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserControllerConverter userControllerConverter;

    public AuthController(AuthService authService,
            UserControllerConverter userControllerConverter) {
        this.authService = authService;
        this.userControllerConverter = userControllerConverter;
    }

    @PostMapping("")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        // WHY:
        // Validation des entrées (évite appels invalides au service)
        if (request == null ||
                request.getLogin() == null ||
                request.getPassword() == null) {

            ErrorCode.INVALID_REQUEST.throwException();
        }
        return ResponseEntity.ok(toResponse(authService.auth(request.getLogin(), request.getPassword())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        if (request == null || request.getRefreshToken() == null) {
            ErrorCode.INVALID_REQUEST.throwException();
        }

        return ResponseEntity.ok(toResponse(authService.refresh(request.getRefreshToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        if (request == null || request.getRefreshToken() == null) {
            ErrorCode.INVALID_REQUEST.throwException();
        }

        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRequest request) {
        if (request == null)
            ErrorCode.INVALID_REQUEST.throwException();
        UserEntity entity = userControllerConverter.fromRequestToEntity(request);

        authService.register(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private AuthResponse toResponse(AuthTokens authTokens) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(authTokens.getAccessToken());
        response.setRefreshToken(authTokens.getRefreshToken());
        return response;
    }
}
