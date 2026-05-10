package com.example.Customer.Service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillDTO {
    private Long id;
    private Long contractId;
    private Long customerId;
    private Double consumption;
    private Double totalAmount;
    private String paymentStatus;
    private LocalDate billingDate;
}
