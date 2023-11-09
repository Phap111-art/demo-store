package com.example.projectdemogit.controller;

import com.cloudinary.Cloudinary;
import com.example.projectdemogit.dtos.request.user.CreateUserDto;
import com.example.projectdemogit.dtos.request.user.UpdateUserDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.service.UserService;
import com.example.projectdemogit.utils.CloudinaryUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Value("${cloudinary.folder_avatar}")
    private String folder ;
    private final Cloudinary cloudinary;
    private final UserService userService;


    @PostMapping("/create-user")
    public ResponseEntity<CustomResponse> createUser(@RequestBody @Valid CreateUserDto dto, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto,result));
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
    public ResponseEntity<CustomResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse> deleteByIdUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteByIdUser(id));
    }

    @GetMapping("/find-avatar/{publicId}")
    public ResponseEntity<byte[]> getImageCloudinary(@PathVariable("publicId") String publicId)  {
       return CloudinaryUtil.getImageFromCloudinary(folder,publicId,cloudinary);
    }
    @GetMapping("/upload-avatar/{id}")
    public ResponseEntity<CustomResponse> uploadToCloudinary(@PathVariable("id") String id ,@RequestParam("file") MultipartFile file)  {
        return ResponseEntity.status(HttpStatus.OK).body(userService.uploadAvatar(file,id));
    }
}
