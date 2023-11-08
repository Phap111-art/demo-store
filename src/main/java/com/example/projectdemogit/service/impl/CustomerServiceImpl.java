package com.example.projectdemogit.service.impl;

import com.example.projectdemogit.dtos.request.customer.CreateCustomerDTO;
import com.example.projectdemogit.dtos.request.customer.UpdateCustomerDTO;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.Customer;
import com.example.projectdemogit.exception.CustomException;
import com.example.projectdemogit.mapper.DataMapper;
import com.example.projectdemogit.repository.CustomerRepository;
import com.example.projectdemogit.service.CustomerService;
import com.example.projectdemogit.utils.ConvertStringToUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomResponse getAllCustomers() {
        try {
            List<Customer> customers = customerRepository.findAll();
            if (customers.isEmpty()) {
                throw new CustomException("data customer is null in db !", HttpStatus.NOT_FOUND);
            }
            return new CustomResponse("get all customer successfully !", HttpStatus.OK.value(), customers);

        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), customerRepository.findAll());
        }
    }

    @Override
    public CustomResponse getCustomerById(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<Customer> customer = customerRepository.findById(uuid);
            if (customer.isPresent()) {
                return new CustomResponse("Found Id " + id + " successfully", HttpStatus.OK.value(), customer.get());
            } else {
                throw new IllegalArgumentException("Not found id customer: " + id);
            }
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }

    @Override
    public CustomResponse createCustomer(CreateCustomerDTO dto) {

        try {
            Customer customerEntity = DataMapper.toEntity(dto, Customer.class);
            Customer savedCustomer = customerRepository.save(customerEntity);

            return new CustomResponse("Save Successfully", HttpStatus.CREATED.value(),
                    DataMapper.toDTO(savedCustomer, CreateCustomerDTO.class));
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), new CreateCustomerDTO());
        }
    }
    private boolean isNameNumeric(CreateCustomerDTO dto) {
        String name = dto.getName();
        try {
            Double.parseDouble(name);
            return true; // Nếu name là số, trả về true
        } catch (NumberFormatException e) {
            return false; // Nếu name không phải là số, trả về false
        }
    }
    @Override
    public CustomResponse updateCustomer(String id, UpdateCustomerDTO dto) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<Customer> existingCustomer = customerRepository.findById(uuid);
            if (existingCustomer.isPresent()) {
                Customer updatedCustomerEntity = DataMapper.toEntity(dto, Customer.class);
                updatedCustomerEntity.setCustomerId(uuid);
                return new CustomResponse("Customer updated successfully!", HttpStatus.OK.value(),
                        DataMapper.toDTO(customerRepository.save(updatedCustomerEntity), UpdateCustomerDTO.class));
            } else {
                throw new IllegalArgumentException("Update failed! Customer not found: " + id);
            }
        } catch (IllegalArgumentException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), new UpdateCustomerDTO());
        }
    }

    @Override
    public CustomResponse deleteCustomer(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<Customer> existingCustomer = customerRepository.findById(uuid);
            if (existingCustomer.isPresent()) {
                customerRepository.deleteById(uuid);
                return new CustomResponse("Customer deleted successfully!", HttpStatus.ACCEPTED.value(), "");
            } else {
                throw new IllegalArgumentException("Customer not found: " + id);
            }
        } catch (RuntimeException e) {
            return new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        }
    }
}