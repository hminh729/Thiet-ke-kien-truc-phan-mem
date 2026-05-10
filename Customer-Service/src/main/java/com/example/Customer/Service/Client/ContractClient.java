package com.example.Customer.Service.Client;

import com.example.Customer.Service.dto.ContractDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "contract-service")
public interface ContractClient {
    @GetMapping("/api/v1/contracts/all")
    List<ContractDTO> getAllContracts();
}
