package com.example.Meter.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadingDTO {
    private Long id;
    private Long apartmentId;
    private Double readingValue;
    private LocalDate readingDate;
}
