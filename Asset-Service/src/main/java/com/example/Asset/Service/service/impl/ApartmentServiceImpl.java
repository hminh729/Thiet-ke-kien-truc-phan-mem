package com.example.Asset.Service.service.impl;

import com.example.Asset.Service.dto.ApartmentDTO;
import com.example.Asset.Service.entity.Apartment;
import com.example.Asset.Service.entity.ApartmentStatus;
import com.example.Asset.Service.repository.ApartmentRepository;
import com.example.Asset.Service.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public List<ApartmentDTO> getAllApartments() {
        return apartmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentDTO> getAvailableApartments() {
        return apartmentRepository.findByStatus(ApartmentStatus.AVAILABLE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApartmentDTO getApartmentById(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));
        return convertToDTO(apartment);
    }

    @Override
    public ApartmentDTO createApartment(ApartmentDTO apartmentDTO) {
        Apartment apartment = convertToEntity(apartmentDTO);
        Apartment saved = apartmentRepository.save(apartment);
        return convertToDTO(saved);
    }

    @Override
    public ApartmentDTO updateApartmentStatus(Long id, ApartmentStatus status) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));
        apartment.setStatus(status);
        Apartment updated = apartmentRepository.save(apartment);
        return convertToDTO(updated);
    }

    private ApartmentDTO convertToDTO(Apartment apartment) {
        return ApartmentDTO.builder()
                .id(apartment.getId())
                .roomNumber(apartment.getRoomNumber())
                .block(apartment.getBlock())
                .floor(apartment.getFloor())
                .area(apartment.getArea())
                .basePrice(apartment.getBasePrice())
                .status(apartment.getStatus())
                .description(apartment.getDescription())
                .waterStatus(apartment.getWaterStatus())
                .build();
    }

    private Apartment convertToEntity(ApartmentDTO dto) {
        return Apartment.builder()
                .id(dto.getId())
                .roomNumber(dto.getRoomNumber())
                .block(dto.getBlock())
                .floor(dto.getFloor())
                .area(dto.getArea())
                .basePrice(dto.getBasePrice())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .waterStatus(dto.getWaterStatus())
                .build();
    }
}