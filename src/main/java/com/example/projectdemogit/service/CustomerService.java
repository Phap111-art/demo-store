package com.example.projectdemogit.service;

import com.example.projectdemogit.dtos.request.customer.CreateCustomerDTO;
import com.example.projectdemogit.dtos.request.customer.UpdateCustomerDTO;
import com.example.projectdemogit.dtos.response.CustomResponse;
import org.springframework.validation.BindingResult;

public interface CustomerService {
    CustomResponse getAllCustomers();
    CustomResponse getCustomerById(String id);
    CustomResponse createCustomer(CreateCustomerDTO createCustomerDTO );
    CustomResponse updateCustomer(String id, UpdateCustomerDTO updatedCustomerDTO);
    CustomResponse deleteCustomer(String id);
}