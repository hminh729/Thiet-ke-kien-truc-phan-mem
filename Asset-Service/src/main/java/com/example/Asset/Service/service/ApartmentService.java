package com.example.Asset.Service.service;

import com.example.Asset.Service.dto.ApartmentDTO;
import com.example.Asset.Service.entity.ApartmentStatus;

import java.util.List;

public interface ApartmentService {
    List<ApartmentDTO> getAllApartments();

    List<ApartmentDTO> getAvailableApartments();

    ApartmentDTO getApartmentById(Long id);

    ApartmentDTO createApartment(ApartmentDTO apartmentDTO);

    ApartmentDTO updateApartmentStatus(Long id, ApartmentStatus status);
}