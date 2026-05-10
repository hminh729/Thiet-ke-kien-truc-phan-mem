package com.example.Customer.Service.Client;

import com.example.Customer.Service.dto.ApartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "asset-service")
public interface AssetClient {
    @GetMapping("/api/v1/assets")
    List<ApartmentDTO> getAllApartments();
}
