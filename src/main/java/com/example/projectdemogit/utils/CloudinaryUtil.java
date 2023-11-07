package com.example.projectdemogit.utils;


import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.projectdemogit.exception.CustomCloudinaryException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public class CloudinaryUtil {
    public static String uploadFileToCloudinary(Cloudinary cloudinary, MultipartFile file, String folder) throws CustomCloudinaryException {
        String publicId = folder + "/" + file.getOriginalFilename();
        try {
            // Lọc tất cả tài nguyên nào có tồn tại publicId trên cloudinary không
            Api api = cloudinary.api();
            Map<?, ?> resourcesResult = api.resources(ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folder
            ));
            List<Map<?, ?>> resources = (List<Map<?, ?>>) resourcesResult.get("resources");
            boolean fileExists = false;
            for (Map<?, ?> resource : resources) {
                String currentPublicId = (String) resource.get("public_id");
                if (currentPublicId.equals(publicId)) {
                    fileExists = true;
                    break;
                }
            }
            if (fileExists) {
                // File đã tồn tại
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "public_id", publicId,
                        "overwrite", true
                ));
                return publicId.substring(folder.length() + 1); // Trả về publicId của hình ảnh (đã cắt bỏ phần folder + "/")
            } else {
                // File chưa tồn tại
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "public_id", publicId,
                        "overwrite", false
                ));
                return publicId.substring(folder.length() + 1); // Trả về publicId của hình ảnh (đã cắt bỏ phần folder + "/")
            }
        } catch (IOException e) {
            throw new CustomCloudinaryException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity<byte[]> getImageFromCloudinary(String folder, String photo,Cloudinary cloudinary) {
        String publicId = folder + "/" + photo;
        String url = cloudinary.url().generate(publicId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, httpEntity, byte[].class);
            return ResponseEntity.ok()
                    .contentLength(response.getBody().length)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 Not Found error
            return ResponseEntity.notFound().build();
        }
    }

}
