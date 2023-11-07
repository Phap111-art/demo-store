package com.example.projectdemogit.service.impl;

import com.cloudinary.Cloudinary;
import com.example.projectdemogit.auth.CustomUserDetails;
import com.example.projectdemogit.dtos.request.user.CreateUserDto;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.exception.CustomAuthenticationException;
import com.example.projectdemogit.exception.MultipartFileExample;
import com.example.projectdemogit.exception.ValidationException;
import com.example.projectdemogit.jwt.JwtTokenProvider;
import com.example.projectdemogit.mapper.DataMapper;
import com.example.projectdemogit.oauth2.CustomOidcUser;
import com.example.projectdemogit.repository.UserRepository;
import com.example.projectdemogit.service.RoleService;
import com.example.projectdemogit.service.UserService;
import com.example.projectdemogit.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public CustomResponse createUser(String jsonUser, MultipartFile file) {

        try {
            CreateUserDto dto = JsonUtil.convertJsonToObject(jsonUser, CreateUserDto.class);
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ValidationException("Email already exists!");
            }
//            if (result.hasErrors()) {
//                throw new ValidationException(ValidationUtils.getValidationErrorString(result));
//            }
            if (file.isEmpty() || file == null) {
                throw new MultipartFileExample("File cannot be null");
            }
            /*upload to cloudinary */
            String avatar = CloudinaryUtil.uploadFileToCloudinary(cloudinary, file, cloudinaryFolderProduct);
            /*mapper*/
            User entity = DataMapper.toEntity(dto, User.class);
            /*set BCrypt */
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            /*set Role  */
            entity.setRoles(roleService.getRolesByRoleIds(dto.getRoleId()));
            entity.setAvatar(avatar);
            /*save User */
            User savedUser = userRepository.save(entity);
            return new CustomResponse("User created successfully!", HttpStatus.CREATED.value(), savedUser);
        } catch (Exception e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), new CreateUserDto());
        }
    }

    @Override
    public CustomResponse updateUser(String id, UpdateUserDto dto, BindingResult result) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> existingUser = userRepository.findById(uuid);
            if (result.hasErrors()) {
                throw new ValidationException(ValidationUtils.getValidationErrorString(result));
            }
            if (!existingUser.isPresent()) {
                throw new IllegalArgumentException("Update failed! User not found: " + id);
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
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), new UpdateUserDto());
        }
    }

    @Override
    public CustomResponse validateUserAndGenerateToken(LoginUserDto dto, BindingResult result,UserDetailsService detailsService) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) detailsService.loadUserByUsername(dto.getUsername());
            if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            if (result.hasErrors()) {
                throw new ValidationException(ValidationUtils.getValidationErrorString(result));
            }
            // create JWT
            String token = jwtTokenProvider.generateToken(userDetails);
            return new CustomResponse("User Login successfully!", HttpStatus.CREATED.value(), token);
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), new LoginUserDto());
        }
    }

    @Override
    public CustomResponse findByIdUser(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> user = userRepository.findById(uuid);
            if (!user.isPresent()) {
                throw new IllegalArgumentException("User not found");
            }
            return new CustomResponse("User found", HttpStatus.OK.value(),
                    DataMapper.toDTO(user.get(), CreateUserDto.class));
        } catch (Exception e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }


    @Override
    public CustomResponse deleteByIdUser(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<User> existingUser = userRepository.findById(uuid);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Delete failed! User not found: " + id);
            }
            userRepository.deleteById(uuid);
            return new CustomResponse("User deleted successfully!", HttpStatus.OK.value(), "");
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
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
            return new CustomResponse("Login ok Successfully", HttpStatus.OK.value(), "username : " + username + " - " + roles);
        }
        return new CustomResponse("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(), null);
    }

    @Override
    public void createNewOrUpdateUserOAuth2(String email) {
        Optional<User> existingUser = findByEmail(email);
        if (!existingUser.isPresent()) {
            Role role = Role.builder().roleId(2).build();
            userRepository.save(User.builder().email(email).isActive(true).roles(Set.of(role)).build());
        } else {
            userRepository.save(existingUser.get());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
