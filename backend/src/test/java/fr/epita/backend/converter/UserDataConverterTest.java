package fr.epita.backend.converter;

import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.domain.entity.UserEntity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserDataConverterTest {

    private final UserDataConverter converter = new UserDataConverter();

    @Test
    void fromModelToEntity_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Model is converted into entity
        // ============================================================

        UserModel model = new UserModel();
        model.setLogin("alice");

        UserEntity entity = converter.fromModelToEntity(model);

        assertThat(entity.getLogin()).isEqualTo("alice");
    }

    @Test
    void fromEntityToModel_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Entity is converted into model
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setLogin("alice");

        UserModel model = converter.fromEntityToModel(entity);

        assertThat(model.getLogin()).isEqualTo("alice");
    }

    @Test
    void transfertData_should_copy_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Entity data is copied into existing model
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setLogin("updated");

        UserModel model = new UserModel();

        converter.transfertDataFromEntityToModel(model, entity);

        assertThat(model.getLogin()).isEqualTo("updated");
    }
}