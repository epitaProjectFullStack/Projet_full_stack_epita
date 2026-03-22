package fr.epita.backend.controller.api.response.AdminResponses.UserResponses;

import java.util.List;

import lombok.Data;

@Data
public class AdminUsersResponse {
    private List<AdminUserResponse> list;
}
