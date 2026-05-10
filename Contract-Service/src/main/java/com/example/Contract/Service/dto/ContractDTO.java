package com.example.Contract.Service.dto;

import com.example.Contract.Service.Entity.ContractStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ContractDTO {
    private Long id;
    private Long customerId;
    private Long apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Double deposit;
    private Double waterPrice;
    private ContractStatus status;
}