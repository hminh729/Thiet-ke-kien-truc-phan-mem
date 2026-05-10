package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String idCard;
    private String email;
}