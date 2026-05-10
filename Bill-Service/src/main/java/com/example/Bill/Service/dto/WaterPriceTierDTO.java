package com.example.Bill.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterPriceTierDTO {
    private Long id;
    private Double minVolume;
    private Double maxVolume;
    private Double price;
}
