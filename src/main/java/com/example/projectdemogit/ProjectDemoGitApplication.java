package com.example.projectdemogit;

import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ProjectDemoGitApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ProjectDemoGitApplication.class, args);
    }


}
