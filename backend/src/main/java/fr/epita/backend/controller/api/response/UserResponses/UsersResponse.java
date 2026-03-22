package fr.epita.backend.controller.api.response.UserResponses;

import java.util.List;

import lombok.Data;

@Data
public class UsersResponse {
    private List<UserResponse> list;
}
