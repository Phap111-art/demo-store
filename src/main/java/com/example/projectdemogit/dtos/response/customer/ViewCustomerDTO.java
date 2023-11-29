package com.example.projectdemogit.dtos.response.customer;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCustomerDTO {
    @NotNull
    private UUID customerId;
    @NotNull
    private String name;
    @NotNull
    private String phone;

}
