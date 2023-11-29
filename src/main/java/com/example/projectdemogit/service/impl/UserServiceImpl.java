package com.example.projectdemogit.service.impl;

import com.cloudinary.Cloudinary;
import com.example.projectdemogit.auth.userdetails.CustomUserDetails;
import com.example.projectdemogit.dtos.request.user.CreateUserDto;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.exception.CustomException;
import com.example.projectdemogit.exception.InvalidException;
import com.example.projectdemogit.exception.MultipartFileException;
import com.example.projectdemogit.auth.jwt.JwtTokenProvider;
import com.example.projectdemogit.mapper.DataMapper;
import com.example.projectdemogit.auth.oauth2.CustomOidcUser;
import com.example.projectdemogit.repository.UserRepository;
import com.example.projectdemogit.service.RoleService;
import com.example.projectdemogit.service.UserService;
import com.example.projectdemogit.utils.AuthenticationUtils;
import com.example.projectdemogit.utils.CloudinaryUtil;
import com.example.projectdemogit.utils.ConvertStringToUUID;
import com.example.projectdemogit.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${cloudinary.folder_avatar}")
    private String cloudinaryFolderProduct;
    private final Cloudinary cloudinary;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;


    @Override
    public CustomResponse createUser(CreateUserDto dto, BindingResult result) {

        try {
            if (result.hasErrors()) {
                throw new CustomException(ValidationUtils.getValidationErrorString(result), HttpStatus.BAD_REQUEST);
            }
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new CustomException("Email already exists!", HttpStatus.CONFLICT);
            }
            /*mapper*/
            User entity = DataMapper.toEntity(dto, User.class);
            /*set BCrypt */
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            /*set Role  */
            entity.setRoles(roleService.getRolesByRoleIds(dto.getRoleId()));
            /*save User */
            User savedUser = userRepository.save(entity);
            return new CustomResponse("User created successfully!", HttpStatus.CREATED.value(), savedUser);
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), new CreateUserDto());
        }
    }

    @Override
    public CustomResponse updateUser(String id, UpdateUserDto dto, BindingResult result) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> existingUser = userRepository.findById(uuid);
            if (result.hasErrors()) {
                throw new CustomException(ValidationUtils.getValidationErrorString(result), HttpStatus.BAD_REQUEST);
            }
            if (!existingUser.isPresent()) {
                throw new CustomException("Update failed! User not found: " + id, HttpStatus.NOT_FOUND);
            }
            if (!dto.getPassword().startsWith("$2a$")) {
                dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            User entity = DataMapper.toEntity(dto, User.class);
            entity.setUserId(uuid);
            /*set Role  */
            entity.setRoles(roleService.getRolesByRoleIds(dto.getRoleId()));
            /*update user*/
            User updatedUser = userRepository.save(entity);
            return new CustomResponse("User updated successfully!", HttpStatus.OK.value(), updatedUser);
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), new UpdateUserDto());
        }
    }

    @Override
    public CustomResponse validateUserAndGenerateToken(LoginUserDto dto, BindingResult result, UserDetailsService detailsService) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) detailsService.loadUserByUsername(dto.getUsername());
            if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
                throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }
            if (result.hasErrors()) {
                throw new CustomException(ValidationUtils.getValidationErrorString(result), HttpStatus.BAD_REQUEST);
            }
            // create JWT
            String token = jwtTokenProvider.generateToken(userDetails);
            return new CustomResponse("User Login successfully!", HttpStatus.CREATED.value(), token);
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), new LoginUserDto());
        }
    }

    @Override
    public CustomResponse findByIdUser(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> user = userRepository.findById(uuid);
            if (!user.isPresent()) {
                throw new CustomException("User not found" + id , HttpStatus.NOT_FOUND);
            }
            return new CustomResponse("User found", HttpStatus.OK.value(),
                    DataMapper.toDTO(user.get(), CreateUserDto.class));
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), null);
        }
    }


    @Override
    public CustomResponse deleteByIdUser(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> existingUser = userRepository.findById(uuid);
            if (existingUser.isPresent()) {
                throw new CustomException("Delete failed! User not found: " + id, HttpStatus.NOT_FOUND);
            }
            userRepository.deleteById(uuid);
            return new CustomResponse("User deleted successfully!", HttpStatus.OK.value(), "");
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), null);
        }
    }

    @Override
    public CustomResponse findByUsername(String username) {
        try {
            if (!userRepository.existsByUsername(username)) {
                throw new CustomException("username not found: " + username + " in db !", HttpStatus.NOT_FOUND);
            }
            return new CustomResponse("find username : " + username + " successfully!", HttpStatus.OK.value(), userRepository.findByUsername(username));
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public CustomResponse getUserInfoAfterAuthentication(Authentication authentication, CustomOidcUser customOidcUser) {
        /*get info Oauth2*/
        if (authentication instanceof OAuth2AuthenticationToken && customOidcUser != null) {
            String email = customOidcUser.getEmail();
            String role = customOidcUser.getAuthorities().iterator().next().getAuthority();
            return new CustomResponse("Login with google Successfully",
                    HttpStatus.OK.value(), "email : " + email + " - role :" + role);
        }
        /*get info User*/
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = authentication.getName(); // Lấy tên người dùng từ xác thực
            List<String> roles = AuthenticationUtils.getRoles(customUserDetails);
            return new CustomResponse("Login ok Successfully", HttpStatus.OK.value(), "username : " + username
                    + " - " + roles);
        }
        return new CustomResponse("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(), null);
    }

    @Override
    public CustomResponse findByEmail(String email) {
        try {
            if (!userRepository.existsByEmail(email)) {
                throw new CustomException("Email not found in db!", HttpStatus.NOT_FOUND);
            }
            return new CustomResponse("find your email : " + email + " successfully!", HttpStatus.OK.value(), userRepository.findByEmail(email));
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public CustomResponse uploadAvatar(MultipartFile file, String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> existingUser = userRepository.findById(uuid);
            if (file == null) {
                throw new MultipartFileException("File cannot be null", HttpStatus.BAD_REQUEST);
            }
            if (!existingUser.isPresent()) {
                throw new CustomException("Update failed! User not found: " + id, HttpStatus.NOT_FOUND);
            }
            /*upload to cloudinary */
            String avatar = CloudinaryUtil.uploadFileToCloudinary(cloudinary, file, cloudinaryFolderProduct);
            /*set avatar*/
            existingUser.get().setAvatar(avatar);
            return new CustomResponse("Upload avatar successfully !", HttpStatus.OK.value(), userRepository.save(existingUser.get()));
        } catch (MultipartFileException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), "");
        }
    }

}
