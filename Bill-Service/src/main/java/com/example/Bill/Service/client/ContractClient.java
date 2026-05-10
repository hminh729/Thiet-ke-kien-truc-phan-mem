package com.example.Bill.Service.client;

import com.example.Bill.Service.dto.ContractDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "contract-service")
public interface ContractClient {
    @GetMapping("/api/v1/contracts/{id}")
    ContractDTO getContractById(@PathVariable("id") Long id);
}
