package com.example.Customer.Service.service;

import com.example.Customer.Service.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);
    CustomerDTO getCustomerByPhone(String phone);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    CustomerDTO createCustomer(CustomerDTO customerDTO);
}