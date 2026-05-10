package com.example.Asset.Service.repository;

import com.example.Asset.Service.entity.Apartment;
import com.example.Asset.Service.entity.ApartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByStatus(ApartmentStatus status);
}