package com.example.Asset.Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartments")
@Data
@Builder
@NoArgsConstructor // BẮT BUỘC có cái này
@AllArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    private String block;

    private Integer floor;

    private Double area;

    private Double basePrice;

    @Enumerated(EnumType.STRING)
    private ApartmentStatus status;

    private String description;
    
    private String waterStatus;
}