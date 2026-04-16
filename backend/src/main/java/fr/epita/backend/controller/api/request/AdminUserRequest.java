package fr.epita.backend.controller.api.request;

import fr.epita.backend.utils.Role;
import lombok.Data;

@Data
public class AdminUserRequest {
    private String login;
    private String password;
    private String mail;
    private Role role;
    private boolean banned;
}
