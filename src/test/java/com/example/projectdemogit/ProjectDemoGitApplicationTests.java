package com.example.projectdemogit;

import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ProjectDemoGitApplicationTests {

//
//    @Value("${cloudinary.folder_product}")
//    private String cloudinaryFolderProduct;
//
//    @Autowired
//    private Cloudinary cloudinary;

    //    @Autowired
//    private UserRepository userRepository;
    @Value("${authority_to_access_url}")
    private String[] authorityToAccessUrls;

    @Test
    void contextLoads() {
//        MultipartFile file =  ConvertFileToMultipartFile.get("upload/image1.png");
//        CloudinaryUtil.uploadFileToCloudinary(cloudinary,file,cloudinaryFolderProduct);
//        Optional<User> user = userRepository.findByEmail("macdinhphap123@gmail.com");
//        System.out.println(user.get().getRoles());
//        System.out.println(user.get().getRoles().getClass().getName());
        for (String url : authorityToAccessUrls
             ) {
            System.out.println(url);
        }
    }

}
