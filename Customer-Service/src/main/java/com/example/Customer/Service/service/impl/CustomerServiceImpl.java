package com.example.Customer.Service.service.impl;

import com.example.Customer.Service.dto.CustomerDTO;
import com.example.Customer.Service.Entity.Customer;
import com.example.Customer.Service.Repository.CustomerRepository;
import com.example.Customer.Service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToDTO(customer);
    }

    @Override
    public CustomerDTO getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        int updatedCount = customerRepository.updateCustomer(
                id,
                customerDTO.getFullName(),
                customerDTO.getPhone(),
                customerDTO.getIdCard(),
                customerDTO.getEmail()
        );
        if (updatedCount == 0) {
            throw new RuntimeException("Customer not found or update failed");
        }
        return getCustomerById(id);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer saved = customerRepository.save(customer);
        return convertToDTO(saved);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .idCard(customer.getIdCard())
                .email(customer.getEmail())
                .build();
    }

    private Customer convertToEntity(CustomerDTO dto) {
        return Customer.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .idCard(dto.getIdCard())
                .email(dto.getEmail())
                .build();
    }
}