package com.example.Meter.Service.service.impl;

import com.example.Meter.Service.dto.MeterReadingDTO;
import com.example.Meter.Service.entity.MeterReading;
import com.example.Meter.Service.repository.MeterReadingRepository;
import com.example.Meter.Service.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterReadingRepository meterReadingRepository;

    @Override
    public MeterReadingDTO addReading(MeterReadingDTO readingDTO) {
        MeterReading reading = MeterReading.builder()
                .apartmentId(readingDTO.getApartmentId())
                .readingValue(readingDTO.getReadingValue())
                .readingDate(readingDTO.getReadingDate() != null ? readingDTO.getReadingDate() : LocalDate.now())
                .build();
        reading = meterReadingRepository.save(reading);
        readingDTO.setId(reading.getId());
        readingDTO.setReadingDate(reading.getReadingDate());
        return readingDTO;
    }

    @Override
    public MeterReadingDTO getLatestReading(Long apartmentId) {
        return meterReadingRepository.findTopByApartmentIdOrderByReadingDateDesc(apartmentId)
                .map(reading -> MeterReadingDTO.builder()
                        .id(reading.getId())
                        .apartmentId(reading.getApartmentId())
                        .readingValue(reading.getReadingValue())
                        .readingDate(reading.getReadingDate())
                        .build())
                .orElse(null);
    }
}
