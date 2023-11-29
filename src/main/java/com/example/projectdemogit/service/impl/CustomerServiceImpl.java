package com.example.projectdemogit.service.impl;

import com.example.projectdemogit.dtos.request.customer.CreateCustomerDTO;
import com.example.projectdemogit.dtos.request.customer.UpdateCustomerDTO;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.dtos.response.customer.ViewCustomerDTO;
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
            List<ViewCustomerDTO> viewDto = DataMapper.toDTOList(customers, ViewCustomerDTO.class);
            if (customers.isEmpty()) {
                throw new CustomException("data customer is null in db !", HttpStatus.NOT_FOUND);
            }
            return new CustomResponse("get all customer successfully !", HttpStatus.OK.value(), viewDto);

        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), customerRepository.findAll());
        }
    }

    @Override
    public CustomResponse getCustomerById(String id) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<Customer> customer = customerRepository.findById(uuid);
            ViewCustomerDTO viewCustomerDTO=DataMapper.toDTO(customer.get(),ViewCustomerDTO.class);
            if (customer.isPresent()) {
                return new CustomResponse("Found Id " + id + " successfully", HttpStatus.OK.value(), viewCustomerDTO);
            } else {
                throw new CustomException("Not found id customer: " + id,HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), null);
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
    @Override
    public CustomResponse updateCustomer(String id, UpdateCustomerDTO dto) {
        try {
            UUID uuid = ConvertStringToUUID.getUUID(id);
            Optional<Customer> existingCustomer = customerRepository.findById(uuid);
            if (existingCustomer.isPresent()) {
                Customer entity = DataMapper.toEntity(dto, Customer.class);//convert dto to entity
                entity.setCustomerId(uuid);
                return new CustomResponse("Customer updated successfully!", HttpStatus.OK.value(),
                        DataMapper.toDTO(customerRepository.save(entity), UpdateCustomerDTO.class));
            } else {
                throw new CustomException("Update failed! Customer not found: " + id,HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), new UpdateCustomerDTO());
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
                throw new CustomException("Customer not found: " + id,HttpStatus.BAD_REQUEST);
            }
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), null);
        }
    }
}