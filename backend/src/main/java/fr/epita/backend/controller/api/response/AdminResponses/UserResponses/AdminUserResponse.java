package fr.epita.backend.controller.api.response.AdminResponses.UserResponses;

import java.util.UUID;

import fr.epita.backend.utils.Role;
import lombok.Data;

@Data
public class AdminUserResponse {
    private UUID id;
    private String login;
    private String password;
    private String mail;
    private Role role;
    private String token;
    private boolean banned;
}
