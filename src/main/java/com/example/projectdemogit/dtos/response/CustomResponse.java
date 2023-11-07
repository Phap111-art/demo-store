package com.example.projectdemogit.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomResponse {
    private String mess;
    private int httpStatus;
    private Object data;
}
