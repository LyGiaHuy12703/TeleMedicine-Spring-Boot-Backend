package org.telemedicine.server.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.repository.MedicalStaffRepository;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    //dduowjc khoi chay khiu du an duoc start
    @Bean
    ApplicationRunner applicationRunner(MedicalStaffRepository medicalStaffRepository) {
        return args -> {
            if (medicalStaffRepository.findAll().isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.toString());

                MedicalStaff admin = new MedicalStaff();
                admin.setRoles(roles);
                admin.setEmail("admin@email.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                medicalStaffRepository.save(admin);

                log.warn("admin has been created with default password: admin, please change it");
            }
        };
    };
}
