package com.example.Bill.Service.dto;

import lombok.Data;

@Data
public class MeterReadingDTO {
    private Long id;
    private Long apartmentId;
    private Double readingValue;
    private String readingDate;
}
