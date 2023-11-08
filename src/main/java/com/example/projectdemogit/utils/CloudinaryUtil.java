package com.example.projectdemogit.utils;


import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
public class CloudinaryUtil {
    private static final String PUBLIC_ID_KEY = "public_id";

    public static String uploadFileToCloudinary(Cloudinary cloudinary, MultipartFile file, String folder)   {
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
                String currentPublicId = (String) resource.get(PUBLIC_ID_KEY);
                if (currentPublicId.equals(publicId)) {
                    fileExists = true;
                    break;
                }
            }
            Map<?, ?> uploadResult;
            if (fileExists) {
                // File already exists
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        PUBLIC_ID_KEY, publicId,
                        "overwrite", true
                ));
                log.info("File already exists. Upload result: {}", uploadResult);
            } else {
                // File does not exist
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        PUBLIC_ID_KEY, publicId,
                        "overwrite", false
                ));
                log.info("File does not exist. Upload result: {}", uploadResult);
            }
            return publicId.substring(folder.length() + 1); // Trả về publicId của hình ảnh (đã cắt bỏ phần folder + "/")
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity<byte[]> getImageFromCloudinary(String folder, String photo, Cloudinary cloudinary) {
        String publicId = folder + "/" + photo;
        String url = cloudinary.url().generate(publicId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, httpEntity, byte[].class);
            byte[] responseBody = response.getBody();
            if (responseBody != null) {
                return ResponseEntity.ok()
                        .contentLength(responseBody.length)
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(responseBody);
            } else {
                // Handle the case when the response body is null
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 Not Found error
            return ResponseEntity.notFound().build();
        } catch (NullPointerException e) {
            // Handle null response body case
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
