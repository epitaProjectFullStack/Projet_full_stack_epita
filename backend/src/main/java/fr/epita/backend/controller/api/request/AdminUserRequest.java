package fr.epita.backend.controller.api.request;

import fr.epita.backend.utils.Role;
import lombok.Data;

@Data
public class AdminUserRequest {
    private String login;
    private Role role;
    private String token;
    private boolean banned;
}
