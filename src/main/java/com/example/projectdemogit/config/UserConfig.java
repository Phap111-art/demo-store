package com.example.projectdemogit.config;

import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UserConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public User getUser() {
        userRepository.deleteAll();
        Role role = Role.builder().roleId(1).build();
        User user = User.builder().username("lan").email("macdinhphap123@gmail.com")
                .password(passwordEncoder.encode("123")).isActive(true)
                .roles(Set.of(role)).build();
        return userRepository.save(user);
    }
}
