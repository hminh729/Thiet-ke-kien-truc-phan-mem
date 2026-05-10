package com.example.Asset.Service.dto;

import com.example.Asset.Service.entity.ApartmentStatus;
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
    private ApartmentStatus status;
    private String description;
    private String waterStatus;
}