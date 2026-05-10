package com.example.Meter.Service.repository;

import com.example.Meter.Service.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    Optional<MeterReading> findTopByApartmentIdOrderByReadingDateDesc(Long apartmentId);
}
