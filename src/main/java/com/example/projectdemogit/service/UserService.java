package com.example.projectdemogit.service;

import com.example.projectdemogit.dtos.request.user.CreateUserDto;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.auth.oauth2.CustomOidcUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    CustomResponse createUser(CreateUserDto dto, BindingResult result);

    CustomResponse updateUser(String id, UpdateUserDto dto, BindingResult result);

    CustomResponse validateUserAndGenerateToken(LoginUserDto dto, BindingResult result, UserDetailsService detailsService);

    CustomResponse findByIdUser(String id);

    CustomResponse deleteByIdUser(String id);

    CustomResponse findByEmail(String email);

    CustomResponse findByUsername(String username);

    CustomResponse getUserInfoAfterAuthentication(Authentication authentication, CustomOidcUser customOidcUser);

    CustomResponse uploadAvatar(MultipartFile file ,String id);


}
