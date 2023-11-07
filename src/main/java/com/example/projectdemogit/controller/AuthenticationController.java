package com.example.projectdemogit.controller;

import com.example.projectdemogit.dtos.request.user.LoginUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.oauth2.CustomOidcUser;
import com.example.projectdemogit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailsService detailsService;
    private final UserService userService;

    @PostMapping("/login-jwt")
    public ResponseEntity<CustomResponse> authenticateUser(@RequestBody @Valid LoginUserDto dto ,BindingResult result) {
        return ResponseEntity.ok(userService.validateUserAndGenerateToken(dto,result,detailsService));
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
    public ResponseEntity<CustomResponse> home(Authentication authentication , @AuthenticationPrincipal CustomOidcUser customOidcUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfoAfterAuthentication(authentication,customOidcUser));
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
