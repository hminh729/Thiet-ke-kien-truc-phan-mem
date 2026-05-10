package com.example.Customer.Service.Client;

import com.example.Customer.Service.dto.BillDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bill-service")
public interface BillClient {
    @GetMapping("/api/v1/bills/customer/{customerId}")
    List<BillDTO> getBillsByCustomer(@PathVariable("customerId") Long customerId);
}
