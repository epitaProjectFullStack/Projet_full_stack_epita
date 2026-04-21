package fr.epita.backend.config;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.utils.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    public CommandLineRunner adminBootstrapRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.login}") String adminLogin,
            @Value("${app.bootstrap.admin.password}") String adminPassword,
            @Value("${app.bootstrap.admin.mail}") String adminMail) {
        return args -> {
            UserModel existingAdmin = userRepository.findByLogin(adminLogin).orElse(null);

            if (existingAdmin != null) {
                existingAdmin.setPassword(passwordEncoder.encode(adminPassword));
                existingAdmin.setMail(adminMail);
                existingAdmin.setRole(Role.ADMINISTRATOR);
                existingAdmin.setBanned(false);
                userRepository.save(existingAdmin);
                return;
            }

            if (userRepository.findByMail(adminMail).isPresent()) {
                return;
            }

            UserModel adminUser = new UserModel();
            adminUser.setLogin(adminLogin);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setMail(adminMail);
            adminUser.setRole(Role.ADMINISTRATOR);
            adminUser.setBanned(false);

            userRepository.save(adminUser);
        };
    }
}
