package fr.epita.backend.controller.rest;

import fr.epita.backend.controller.api.request.AuthRequest;
import fr.epita.backend.controller.api.response.AuthResponse;
import fr.epita.backend.converter.ControllerConverter.AuthControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.AuthService;
import fr.epita.backend.utils.ErrorCode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final AuthControllerConverter authConverter;

    public AuthController(AuthService authService, AuthControllerConverter authConverter) {
        this.authService = authService;
        this.authConverter = authConverter;
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        if (request == null)
            ErrorCode.BAD_CREDENTIAL.throwException();
        UserEntity user = authService.auth(request.getLogin(), request.getPassword());
        AuthResponse response = authConverter.FromEntityToAuthResponse(user);
        return ResponseEntity.ok(response);
    }

}
