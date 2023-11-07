package com.example.projectdemogit.service;

import com.example.projectdemogit.auth.CustomUserDetails;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.oauth2.CustomOidcUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    CustomResponse createUser(String jsonUser, MultipartFile file);
    CustomResponse updateUser(String id, UpdateUserDto dto , BindingResult result);
    CustomResponse validateUserAndGenerateToken(LoginUserDto dto , BindingResult result, UserDetailsService detailsService);
    CustomResponse findByIdUser(String id);
    CustomResponse deleteByIdUser(String id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    CustomResponse getUserInfoAfterAuthentication(Authentication authentication , CustomOidcUser customOidcUser);
    void createNewOrUpdateUserOAuth2(String email);


}
