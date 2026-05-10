package com.example.Contract.Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private Long apartmentId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    private Double deposit;

    private Double waterPrice;

    @Enumerated(EnumType.STRING)
    private com.example.Contract.Service.Entity.ContractStatus status;
}