package com.example.Contract.Service.service;

import com.example.Contract.Service.dto.ContractDTO;

import java.util.List;

public interface ContractService {
    ContractDTO createContract(ContractDTO contractDTO);

    List<ContractDTO> getContractsByCustomer(Long customerId);

    ContractDTO terminateContract(Long id);

    List<ContractDTO> getActiveContracts();

    List<ContractDTO> getAllContracts();

    ContractDTO getContractById(Long id);
}