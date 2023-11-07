package com.example.projectdemogit.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConvertFileToMultipartFile {

    public static MultipartFile get(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return new MockMultipartFile(file.getName(), file.getName(), null, fileInputStream);
    }

}