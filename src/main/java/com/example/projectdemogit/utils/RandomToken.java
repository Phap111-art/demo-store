package com.example.projectdemogit.utils;
import org.thymeleaf.util.StringUtils;

public class RandomToken {
    public static String get(){
        return StringUtils.randomAlphanumeric(45);
    }
}
