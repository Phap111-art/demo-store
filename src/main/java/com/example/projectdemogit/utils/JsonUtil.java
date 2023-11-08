package com.example.projectdemogit.utils;

import com.example.projectdemogit.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public class JsonUtil {
    public static <T> T convertJsonToObject(String json, Class<T> objectClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new CustomException(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
