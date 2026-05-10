package com.example.Bill.Service.client;

import com.example.Bill.Service.dto.MeterReadingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "meter-service")
public interface MeterClient {
    @GetMapping("/api/v1/meters/{apartmentId}/latest")
    MeterReadingDTO getLatestReading(@PathVariable("apartmentId") Long apartmentId);

    @org.springframework.web.bind.annotation.PostMapping("/api/v1/meters/readings")
    MeterReadingDTO addReading(MeterReadingDTO readingDTO);
}
