package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractDTO {
    private Long id;
    private Long customerId;
    private Long apartmentId;
    private String status; // Simplified as String
}