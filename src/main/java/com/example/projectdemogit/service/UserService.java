package com.example.projectdemogit.service;

import com.example.projectdemogit.auth.CustomUserDetails;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    CustomResponse createUser(String jsonUser, MultipartFile file);
    CustomResponse updateUser(String id, UpdateUserDto dto , BindingResult result);
    CustomResponse validateUserAndGenerateToken(LoginUserDto dto , BindingResult result, CustomUserDetails cus);
    CustomResponse findByIdUser(String id);
    CustomResponse deleteByIdUser(String id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    void createNewOrUpdateUserOAuth2(String email);


}
