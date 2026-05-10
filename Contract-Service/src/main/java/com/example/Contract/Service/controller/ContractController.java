package com.example.Contract.Service.controller;

import com.example.Contract.Service.dto.ContractDTO;
import com.example.Contract.Service.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ContractDTO createContract(@RequestBody ContractDTO contractDTO) {
        return contractService.createContract(contractDTO);
    }

    @GetMapping("/customer/{customerId}")
    public List<ContractDTO> getContractsByCustomer(@PathVariable Long customerId) {
        return contractService.getContractsByCustomer(customerId);
    }

    @PatchMapping("/{id}/terminate")
    public ContractDTO terminateContract(@PathVariable Long id) {
        return contractService.terminateContract(id);
    }

    @GetMapping("/active")
    public List<ContractDTO> getActiveContracts() {
        return contractService.getActiveContracts();
    }

    @GetMapping("/all")
    public List<ContractDTO> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{id}")
    public ContractDTO getContractById(@PathVariable Long id) {
        return contractService.getContractById(id);
    }
}