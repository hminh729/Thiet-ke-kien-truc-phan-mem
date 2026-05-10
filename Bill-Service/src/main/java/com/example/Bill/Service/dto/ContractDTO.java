package com.example.Bill.Service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {
    private Long id;
    private Long customerId;
    private Long apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Double deposit;
    private Double waterPrice;
    private String status;
}
