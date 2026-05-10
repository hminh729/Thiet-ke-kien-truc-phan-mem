package com.example.Asset.Service.controller;

import com.example.Asset.Service.dto.ApartmentDTO;
import com.example.Asset.Service.entity.ApartmentStatus;
import com.example.Asset.Service.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public List<ApartmentDTO> getAllApartments() {
        return apartmentService.getAllApartments();
    }

    @GetMapping("/available")
    public List<ApartmentDTO> getAvailableApartments() {
        return apartmentService.getAvailableApartments();
    }

    @GetMapping("/{id}")
    public ApartmentDTO getApartmentById(@PathVariable Long id) {
        return apartmentService.getApartmentById(id);
    }

    @PostMapping
    public ApartmentDTO createApartment(@RequestBody ApartmentDTO apartmentDTO) {
        return apartmentService.createApartment(apartmentDTO);
    }

    @PatchMapping("/{id}/status")
    public ApartmentDTO updateApartmentStatus(@PathVariable Long id, @RequestParam ApartmentStatus status) {
        return apartmentService.updateApartmentStatus(id, status);
    }
}