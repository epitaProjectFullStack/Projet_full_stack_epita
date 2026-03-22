package fr.epita.backend.controller.api.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
