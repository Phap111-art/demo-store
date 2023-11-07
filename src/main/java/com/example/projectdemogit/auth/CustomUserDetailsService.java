package com.example.projectdemogit.auth;

import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("*Could not find username!");
        }
        return new CustomUserDetails(user.get());
    }
}
