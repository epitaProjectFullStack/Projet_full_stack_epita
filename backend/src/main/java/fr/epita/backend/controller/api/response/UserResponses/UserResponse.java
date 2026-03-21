package fr.epita.backend.controller.api.response.UserResponses;

import fr.epita.backend.utils.Role;
import lombok.Data;

@Data
public class UserResponse {
    private String login;
    private String password; // Probablement a retirer
    private String mail;
    private Role role;
    private String token;
    private boolean banned;
}
