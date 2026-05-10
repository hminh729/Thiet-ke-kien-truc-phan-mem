package com.example.Meter.Service.controller;

import com.example.Meter.Service.dto.MeterReadingDTO;
import com.example.Meter.Service.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;

    @PostMapping("/readings")
    public MeterReadingDTO addReading(@RequestBody MeterReadingDTO readingDTO) {
        return meterService.addReading(readingDTO);
    }

    @GetMapping("/{apartmentId}/latest")
    public MeterReadingDTO getLatestReading(@PathVariable Long apartmentId) {
        return meterService.getLatestReading(apartmentId);
    }
}
