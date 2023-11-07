package com.example.projectdemogit.oauth2;

import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);
        List<GrantedAuthority> authorities = userOptional.map(user -> user.getRoles().stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()))
                .orElseGet(() -> {
                    Role role = Role.builder().roleId(2).name("USER").build();
                    User user = User.builder().email(email)
                            .password("").isActive(true)
                            .roles(Set.of(role)).build();
                    userRepository.save(user);
                    return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
                });
        // Create a new instance of CustomOidcUser with the updated authorities list
        CustomOidcUser updatedUser = new CustomOidcUser(
                oidcUser.getAttributes(),
                oidcUser.getIdToken(),
                authorities
        );
        log.info("CustomOidcUser attributes: {}", oidcUser.getAttributes());
        log.info("CustomOidcUser idToken: {}", oidcUser.getIdToken());
        log.info("CustomOidcUser authorities: {}", authorities);
        log.info("CustomOidcUser email: {}", email);
        return updatedUser;
    }
}