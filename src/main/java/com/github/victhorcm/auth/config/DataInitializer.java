package com.github.victhorcm.auth.config;

import com.github.victhorcm.auth.model.Role;
import com.github.victhorcm.auth.model.User;
import com.github.victhorcm.auth.repository.RoleRepository;
import com.github.victhorcm.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   RoleRepository roleRepository, 
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            
          
            if (roleRepository.count() == 0) {
                
               
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                
                Role userRole = new Role();
                userRole.setName("ROLE_USER");

                roleRepository.save(adminRole);
                roleRepository.save(userRole);

             
                User admin = new User();
                admin.setEmail("admin@email.com");
                admin.setPasswordHash(passwordEncoder.encode("admin123")); 
                admin.setRoles(Set.of(adminRole));
                // admin.setRoles(Set.of(adminRole, userRole)); // Exemplo com 2 roles

                userRepository.save(admin);

               
                User user = new User();
                user.setEmail("usuario@email.com");
                user.setPasswordHash(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(userRole)); 

                userRepository.save(user);

                System.out.println("--- BANCO POPULADO COM SUCESSO! ---");
                System.out.println("Admin: admin@email.com / admin123");
                System.out.println("User: usuario@email.com / user123");
            }
        };
    }
}