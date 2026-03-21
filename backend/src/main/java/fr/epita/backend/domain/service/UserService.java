package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.utils.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.epita.backend.converter.DataConverter.UserDataConverter;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDataConverter userDataConverter;

    public UserService(UserRepository userRepository, UserDataConverter userDataConverter) {
        this.userRepository = userRepository;
        this.userDataConverter = userDataConverter;
    }

    public List<UserEntity> getUsers() {
        List<UserEntity> userEntitiesList = new ArrayList<UserEntity>();
        List<UserModel> userModelList = userRepository.findAll();

        for (UserModel model : userModelList) {
            UserEntity entity = userDataConverter.fromModelToEntity(model);
            userEntitiesList.add(entity);
        }
        return userEntitiesList;
    }

    public UserEntity getUser(UUID id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(ErrorCode.UNREGISTERED::toException);
        return userDataConverter.fromModelToEntity(userModel);
    }

    public UserEntity updateUser(UUID id, UserEntity entity) {
        UserModel userModel = userRepository.findById(id).orElseThrow(ErrorCode.UNREGISTERED::toException);
        userDataConverter.transfertDataFromEntityToModel(userModel, entity);
        userRepository.save(userModel);
        return userDataConverter.fromModelToEntity(userModel);
    }

    public void deleteUser(UUID id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(ErrorCode.UNREGISTERED::toException);
        userRepository.delete(userModel);
    }
}
