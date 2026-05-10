package com.example.Contract.Service.client;

import com.example.Contract.Service.dto.ApartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "asset-service")
public interface AssetClient {

    @GetMapping("/api/v1/assets/{id}")
    ApartmentDTO getApartmentById(@PathVariable Long id);

    @PatchMapping("/api/v1/assets/{id}/status")
    ApartmentDTO updateApartmentStatus(@PathVariable Long id, @RequestParam String status);
}