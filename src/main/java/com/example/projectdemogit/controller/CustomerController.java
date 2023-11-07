package com.example.projectdemogit.controller;


import com.example.projectdemogit.dtos.request.customer.CreateCustomerDTO;
import com.example.projectdemogit.dtos.request.customer.UpdateCustomerDTO;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.service.CustomerService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CustomResponse> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerDTO dto, BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse> updateCustomer(@PathVariable String id, @RequestBody @Valid UpdateCustomerDTO dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse> deleteCustomer(@PathVariable String id) {
        return ResponseEntity.accepted().body(customerService.deleteCustomer(id));
    }
}