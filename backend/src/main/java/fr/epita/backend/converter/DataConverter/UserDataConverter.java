package fr.epita.backend.converter.DataConverter;

import org.springframework.stereotype.Component;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.domain.entity.UserEntity;

@Component
public class UserDataConverter {
    public UserEntity fromModelToEntity(UserModel model) {

        UserEntity entity = new UserEntity();
        entity.setId(model.getUuid());
        entity.setLogin(model.getLogin());
        entity.setPassword(model.getPassword());
        entity.setMail(model.getMail());
        entity.setRole(model.getRole());
        entity.setToken(model.getToken());
        entity.setBanned(model.isBanned());
        return entity;
    }

    public UserModel fromEntityToModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setUuid(entity.getId());
        model.setLogin(entity.getLogin());
        model.setPassword(entity.getPassword());
        model.setMail(entity.getMail());
        model.setRole(entity.getRole());
        model.setToken(entity.getToken());
        model.setBanned(entity.isBanned());
        return model;
    }

    public void transfertDataFromEntityToModel(UserModel model, UserEntity entity) {
        model.setLogin(entity.getLogin());
        model.setPassword(entity.getPassword());
        model.setMail(entity.getMail());
        model.setRole(entity.getRole());
        model.setToken(entity.getToken());
        model.setBanned(entity.isBanned());
    }

}
