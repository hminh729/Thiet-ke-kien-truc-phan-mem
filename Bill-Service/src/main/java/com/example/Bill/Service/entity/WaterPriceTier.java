package com.example.Bill.Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "water_price_tiers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterPriceTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double minVolume;
    private Double maxVolume; // Null if no upper limit
    private Double price;
}
