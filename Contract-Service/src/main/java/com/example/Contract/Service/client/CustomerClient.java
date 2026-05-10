package com.example.Contract.Service.client;

import com.example.Contract.Service.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Customer-Service")
public interface CustomerClient {

    @GetMapping("/api/v1/customers/id/{id}")
    CustomerDTO getCustomerById(@PathVariable Long id);
}