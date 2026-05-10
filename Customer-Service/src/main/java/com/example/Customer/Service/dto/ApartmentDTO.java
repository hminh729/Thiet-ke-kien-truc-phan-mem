package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApartmentDTO {
    private Long id;
    private String roomNumber;
    private String block;
    private Integer floor;
    private Double area;
    private Double basePrice;
    private String status;
    private String description;
    private String waterStatus; 
}