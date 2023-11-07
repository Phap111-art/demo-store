package com.example.projectdemogit;

import com.cloudinary.Cloudinary;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.exception.CustomCloudinaryException;
import com.example.projectdemogit.repository.UserRepository;
import com.example.projectdemogit.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
class ProjectDemoGitApplicationTests {

//
//    @Value("${cloudinary.folder_product}")
//    private String cloudinaryFolderProduct;
//
//    @Autowired
//    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
//        MultipartFile file =  ConvertFileToMultipartFile.get("upload/image1.png");
//        CloudinaryUtil.uploadFileToCloudinary(cloudinary,file,cloudinaryFolderProduct);
        Optional<User> user = userRepository.findByEmail("macdinhphap123@gmail.com");
        System.out.println(user.get().getRoles());
        System.out.println(user.get().getRoles().getClass().getName());
    }

}
