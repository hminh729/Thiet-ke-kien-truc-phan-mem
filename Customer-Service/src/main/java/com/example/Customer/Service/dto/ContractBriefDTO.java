package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractBriefDTO {
    private Long id;
    private Long customerId;
    private Long apartmentId;
    private String status;
}