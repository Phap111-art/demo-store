package com.example.projectdemogit.controller;

import com.example.projectdemogit.auth.CustomUserDetails;
import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.oauth2.CustomOidcUser;
import com.example.projectdemogit.service.UserService;
import com.example.projectdemogit.utils.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;


@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login-jwt")
    public ResponseEntity<CustomResponse> authenticateUser(@RequestBody @Valid LoginUserDto dto ,BindingResult result) {
        // Xác thực thông tin đăng nhập
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        // Lấy thông tin người dùng từ đối tượng Authentication
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // response JWT
        return ResponseEntity.ok(userService.validateUserAndGenerateToken(dto,result,userDetails));
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomResponse> admin() {
        return ResponseEntity.ok(new CustomResponse("Welcome come admin", HttpStatus.OK.value(), ""));
    }
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CustomResponse> user() {
        return ResponseEntity.ok(new CustomResponse("Welcome come USER", HttpStatus.OK.value(), ""));
    }
    @GetMapping("/warehouse")
    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    public ResponseEntity<CustomResponse> warehouse() {
        return ResponseEntity.ok(new CustomResponse("Welcome come WAREHOUSE_MANAGER", HttpStatus.OK.value(), ""));
    }
    @GetMapping("/seller")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<CustomResponse> seller() {
        return ResponseEntity.ok(new CustomResponse("Welcome come SELLER", HttpStatus.OK.value(), ""));
    }
    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('CUSTOMER_VIP')")
    public ResponseEntity<CustomResponse> customer() {
        return ResponseEntity.ok(new CustomResponse("Welcome come CUSTOMER_VIP", HttpStatus.OK.value(), ""));
    }
    @GetMapping("/auth/403")
    public ResponseEntity<String> accessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }
    @GetMapping("/home")
    public ResponseEntity<?> home(Authentication authentication , Principal principal , @AuthenticationPrincipal CustomOidcUser customOidcUser) {
        /*get info Oauth2*/
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            String email = oauthToken.getPrincipal().getAttribute("email");
            String role = customOidcUser.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Login ok Successfully",
                    HttpStatus.OK.value(), "email : "  + email + " - role :" + role));
        }
        /*get info User*/
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = authentication.getName(); // Lấy tên người dùng từ xác thực
            List<String> roles = AuthenticationUtils.getRoles(customUserDetails);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Login ok Successfully", HttpStatus.OK.value(), "username : " + username + " - " + roles));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomResponse("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(), null));

    }
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("/login");
    }
    @GetMapping("/hello")
    public ResponseEntity<CustomResponse> hello() {
        return ResponseEntity.ok(new CustomResponse("Welcome come", HttpStatus.OK.value(), ""));
    }
}
