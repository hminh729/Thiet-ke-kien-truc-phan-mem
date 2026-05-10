package com.example.Contract.Service.service.impl;

import com.example.Contract.Service.client.AssetClient;
import com.example.Contract.Service.client.CustomerClient;
import com.example.Contract.Service.dto.ApartmentDTO;
import com.example.Contract.Service.dto.ContractDTO;
import com.example.Contract.Service.dto.CustomerDTO;
import com.example.Contract.Service.Entity.Contract;
import com.example.Contract.Service.Entity.ContractStatus;
import com.example.Contract.Service.repository.ContractRepository;
import com.example.Contract.Service.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final CustomerClient customerClient;
    private final AssetClient assetClient;

    @Override
    public ContractDTO createContract(ContractDTO contractDTO) {
        // Check customer exists
        CustomerDTO customer = customerClient.getCustomerById(contractDTO.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }

        // Check apartment is available
        ApartmentDTO apartment = assetClient.getApartmentById(contractDTO.getApartmentId());
        if (apartment == null || !"AVAILABLE".equals(apartment.getStatus())) {
            throw new RuntimeException("Apartment not available");
        }

        // Save contract
        Contract contract = convertToEntity(contractDTO);
        contract.setStatus(ContractStatus.ACTIVE);
        Contract saved = contractRepository.save(contract);

        // Update apartment status to OCCUPIED
        assetClient.updateApartmentStatus(contractDTO.getApartmentId(), "OCCUPIED");

        return convertToDTO(saved);
    }

    @Override
    public List<ContractDTO> getContractsByCustomer(Long customerId) {
        return contractRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDTO terminateContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setStatus(ContractStatus.TERMINATED);
        Contract updated = contractRepository.save(contract);

        // Update apartment status to AVAILABLE
        assetClient.updateApartmentStatus(contract.getApartmentId(), "AVAILABLE");

        return convertToDTO(updated);
    }

    @Override
    public List<ContractDTO> getActiveContracts() {
        return contractRepository.findByStatus(ContractStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDTO getContractById(Long id) {
        return contractRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private ContractDTO convertToDTO(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .customerId(contract.getCustomerId())
                .apartmentId(contract.getApartmentId())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .price(contract.getPrice())
                .deposit(contract.getDeposit())
                .waterPrice(contract.getWaterPrice())
                .status(contract.getStatus())
                .build();
    }

    private Contract convertToEntity(ContractDTO dto) {
        return Contract.builder()
                .id(dto.getId())
                .customerId(dto.getCustomerId())
                .apartmentId(dto.getApartmentId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .price(dto.getPrice())
                .deposit(dto.getDeposit())
                .waterPrice(dto.getWaterPrice())
                .status(dto.getStatus())
                .build();
    }
}