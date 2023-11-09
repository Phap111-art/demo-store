package com.example.projectdemogit.config;

import com.example.projectdemogit.auth.userdetails.CustomUserDetailsService;
import com.example.projectdemogit.enums.RoleType;
import com.example.projectdemogit.handlers.CustomAccessDeniedHandler;
import com.example.projectdemogit.handlers.JwtSessionStorageLogoutHandler;
import com.example.projectdemogit.auth.jwt.JwtAuthenticationFilter;
import com.example.projectdemogit.auth.oauth2.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Value;
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

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Value("${user_to_access_url}")
    private String[] UserAccessUrls;

    @Value("${authority_to_access_url}")
    private String[] authorityToAccessUrls;

    @Value("${default_success_url}")
    private String successUrls;

    private static final String LOGIN_JWT_URL = "/auth/login-jwt";

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
                        .requestMatchers(UserAccessUrls[0], UserAccessUrls[1], UserAccessUrls[2], UserAccessUrls[3]).permitAll()
                        .requestMatchers(authorityToAccessUrls[0]).hasAuthority(RoleType.USER.name())
                        .requestMatchers(authorityToAccessUrls[1]).hasAuthority(RoleType.ADMIN.name())
                        .requestMatchers(authorityToAccessUrls[2]).hasAuthority(RoleType.WAREHOUSE_MANAGER.name())
                        .requestMatchers(authorityToAccessUrls[3]).hasAuthority(RoleType.SELLER.name())
                        .requestMatchers(authorityToAccessUrls[4]).hasAuthority(RoleType.CUSTOMER_VIP.name())
                        .anyRequest().authenticated()
                ).exceptionHandling(config -> config.accessDeniedHandler(accessDeniedHandler()))
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl(successUrls)
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
                        .defaultSuccessUrl(successUrls)
                        .userInfoEndpoint(info -> info
                                .oidcUserService(oidcUserService())
                        )

                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
