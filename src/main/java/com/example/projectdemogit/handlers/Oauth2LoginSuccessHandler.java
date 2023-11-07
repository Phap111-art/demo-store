package com.example.projectdemogit.handlers;


import com.example.projectdemogit.oauth2.CustomOidcUser;
import com.example.projectdemogit.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    final Logger logger = LoggerFactory.getLogger(Oauth2LoginSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        /*String name = defaultOAuth2User.getAttribute("name");
        String given_name = defaultOAuth2User.getAttribute("given_name");
        String family_name = defaultOAuth2User.getAttribute("family_name");*/
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();

        if (auth.getPrincipal() instanceof CustomOidcUser) {
            CustomOidcUser customOidcUser = (CustomOidcUser) auth.getPrincipal();
            String email = customOidcUser.getAttribute("email");
            userService.createNewOrUpdateUserOAuth2(email);
        }
        super.setDefaultTargetUrl("/auth/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
