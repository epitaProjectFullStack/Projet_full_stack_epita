package fr.epita.backend.controller.api.request;

import lombok.Data;

@Data
public class UserRequest {
    private String login;
    private String password;
    private String mail;
}
