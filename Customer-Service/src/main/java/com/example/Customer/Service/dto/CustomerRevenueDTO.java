package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRevenueDTO {
    private CustomerDTO customer;
    private Double totalAmount;
    private Double paidAmount;
    private Double unpaidAmount;
}
