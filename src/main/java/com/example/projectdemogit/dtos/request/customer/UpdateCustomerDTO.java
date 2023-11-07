package com.example.projectdemogit.dtos.request.customer;

import com.example.projectdemogit.dtos.request.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must have 10 digits")
    private String phone;

    @Valid
    private AddressDto address;

    private LocalDateTime updatedAt;

}