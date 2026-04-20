package fr.epita.backend.converter.ControllerConverter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.epita.backend.controller.api.request.AdminUserRequest;
import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.AdminResponses.UserResponses.AdminUserResponse;
import fr.epita.backend.controller.api.response.AdminResponses.UserResponses.AdminUsersResponse;
import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
import fr.epita.backend.controller.api.response.UserResponses.UsersResponse;
import fr.epita.backend.domain.entity.UserEntity;

@Component
public class UserControllerConverter {
    public UserResponse fromEntityToResponse(UserEntity entity) {
        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setLogin(entity.getLogin());
        response.setMail(entity.getMail());
        response.setRole(entity.getRole());
        return response;
    }

    public AdminUserResponse fromEntityToAdminResponse(UserEntity entity) {
        AdminUserResponse response = new AdminUserResponse();
        response.setId(entity.getId());
        response.setLogin(entity.getLogin());
        response.setPassword(entity.getPassword());
        response.setMail(entity.getMail());
        response.setRole(entity.getRole());
        response.setBanned(entity.isBanned());
        return response;
    }

    public AdminUsersResponse fromEntitiesToAdminUsersResponse(List<UserEntity> entities) {
        AdminUsersResponse usersResponse = new AdminUsersResponse();
        List<AdminUserResponse> responseList = new ArrayList<AdminUserResponse>();
        for (UserEntity entity : entities) {
            AdminUserResponse response = fromEntityToAdminResponse(entity);
            responseList.add(response);
        }
        usersResponse.setList(responseList);
        return usersResponse;
    }

    public UsersResponse fromEntitiesToUsersResponse(List<UserEntity> entities) {
        UsersResponse usersResponse = new UsersResponse();
        List<UserResponse> responseList = new ArrayList<UserResponse>();
        for (UserEntity entity : entities) {
            UserResponse response = fromEntityToResponse(entity);
            responseList.add(response);
        }
        usersResponse.setList(responseList);
        return usersResponse;
    }

    public UserEntity fromRequestToEntity(UserRequest request) {
        UserEntity entity = new UserEntity();
        entity.setLogin(request.getLogin());
        entity.setPassword(request.getPassword());
        entity.setMail(request.getMail());
        return entity;
    }

    public UserEntity fromAdminRequestToEntity(AdminUserRequest request) {
        UserEntity entity = new UserEntity();
        entity.setLogin(request.getLogin());
        entity.setPassword(request.getPassword());
        entity.setMail(request.getMail());
        entity.setRole(request.getRole());
        entity.setBanned(request.isBanned());
        return entity;
    }
}
