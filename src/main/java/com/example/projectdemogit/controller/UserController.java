package com.example.projectdemogit.controller;

import com.cloudinary.Cloudinary;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.service.UserService;
import com.example.projectdemogit.utils.CloudinaryUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
    @Value("${cloudinary.folder_avatar}")
    private String folder ;
    private final Cloudinary cloudinary;
    private final UserService userService;

    @Autowired
    public UserController(Cloudinary cloudinary, UserService userService) {
        this.cloudinary = cloudinary;
        this.userService = userService;
    }


    @PostMapping("/create-user")
    public ResponseEntity<CustomResponse> createUser( String jsonUser,@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(jsonUser,file));
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<CustomResponse> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto dto, BindingResult result) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, dto, result));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CustomResponse> findByIdUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByIdUser(id));
    }
    @GetMapping("/find-email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email).get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse> deleteByIdUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteByIdUser(id));
    }

    @GetMapping("/avatar/{publicId}")
    public ResponseEntity<?> getImageCloudinary(@PathVariable("publicId") String publicId)  {
       return CloudinaryUtil.getImageFromCloudinary(folder,publicId,cloudinary);
    }
}