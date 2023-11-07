package com.example.projectdemogit.config;

import com.example.projectdemogit.auth.CustomUserDetailsService;
import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.handlers.CustomAccessDeniedHandler;
import com.example.projectdemogit.handlers.JwtSessionStorageLogoutHandler;
import com.example.projectdemogit.jwt.JwtAuthenticationFilter;
import com.example.projectdemogit.oauth2.CustomOidcUserService;
import com.example.projectdemogit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService detailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new CustomOidcUserService();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public LogoutHandler jwtSessionStorageLogoutHandler() {
        return new JwtSessionStorageLogoutHandler();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(detailsService());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login-jwt", "/auth/create-user", "/forgot/**", "/oauth2/google/**").permitAll()
                        .requestMatchers("/auth/user").hasAuthority("USER")
                        .requestMatchers("/auth/admin").hasAuthority("ADMIN")
                        .requestMatchers("/auth/warehouse").hasAuthority("WAREHOUSE_MANAGER")
                        .requestMatchers("/auth/seller").hasAuthority("SELLER")
                        .requestMatchers("/auth/customer").hasAuthority("CUSTOMER_VIP")
                        .anyRequest().authenticated()
                ).exceptionHandling(config -> config.accessDeniedHandler(accessDeniedHandler()))
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/auth/home")
                ).httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler(jwtSessionStorageLogoutHandler())
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/auth/home")
                        .userInfoEndpoint(info -> info
                                .oidcUserService(oidcUserService())
                        )
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
