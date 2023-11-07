package com.example.projectdemogit.dtos.request.customer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCustomerDTO {

    @NotBlank(message = "Name is required")
    @NotNull(message = "name not null")
    @NotEmpty(message = "not empty")
    private String name;

    @NotEmpty(message = "not empty")
    @NotNull
    private String phone;

    private LocalDateTime createdAt;


}