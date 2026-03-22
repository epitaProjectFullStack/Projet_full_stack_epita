package fr.epita.backend.controller.rest;

import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.AdminResponses.UserResponses.AdminUserResponse;
import fr.epita.backend.controller.api.response.AdminResponses.UserResponses.AdminUsersResponse;
import fr.epita.backend.controller.api.response.UserResponses.UserResponse;

import fr.epita.backend.converter.ControllerConverter.UserControllerConverter;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.UserService;
import fr.epita.backend.utils.ErrorCode;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    private final UserService userService;
    private final UserControllerConverter userControllerConverter;

    public AdminUserController(UserService userService, UserControllerConverter userControllerConverter) {
        this.userService = userService;
        this.userControllerConverter = userControllerConverter;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        if (request == null)
            ErrorCode.INVALID_REQUEST.throwException();
        UserEntity entity = userControllerConverter.fromRequestToEntity(request);

        UserEntity userEntity = userService.createUser(entity);
        UserResponse response = userControllerConverter.fromEntityToResponse(userEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AdminUsersResponse> getUsers() {
        List<UserEntity> userList = userService.getUsers();
        AdminUsersResponse response = userControllerConverter.fromEntitiesToAdminUsersResponse(userList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> getUser(@PathVariable UUID id) {
        UserEntity userEntity = userService.getUser(id);
        AdminUserResponse response = userControllerConverter.fromEntityToAdminResponse(userEntity);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (request == null)
            ErrorCode.INVALID_REQUEST.throwException();
        UserEntity entity = userControllerConverter.fromRequestToEntity(request);

        UserEntity userEntity = userService.updateUser(id, entity);
        UserResponse response = userControllerConverter.fromEntityToResponse(userEntity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
