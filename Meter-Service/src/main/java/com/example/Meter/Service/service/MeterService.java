package com.example.Meter.Service.service;

import com.example.Meter.Service.dto.MeterReadingDTO;


public interface MeterService {
    MeterReadingDTO addReading(MeterReadingDTO readingDTO);
    MeterReadingDTO getLatestReading(Long apartmentId);
}
