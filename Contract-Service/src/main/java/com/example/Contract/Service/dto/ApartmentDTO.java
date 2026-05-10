package com.example.Contract.Service.dto;

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
    private String status; // String vì từ Asset Service
    private String description;
}