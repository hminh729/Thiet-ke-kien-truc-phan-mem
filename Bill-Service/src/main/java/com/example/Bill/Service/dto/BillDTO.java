package com.example.Bill.Service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BillDTO {
    private Long id;
    private Long contractId;
    private Long customerId;
    private Long userId;
    private Double waterVolume;
    private Double totalAmount;
    private String paymentStatus;
    private LocalDate billingDate;
    private List<WaterPriceTierDTO> priceTiers;
}
