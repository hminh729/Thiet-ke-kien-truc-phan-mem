package com.example.Bill.Service.repository;

import com.example.Bill.Service.entity.WaterPriceTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterPriceTierRepository extends JpaRepository<WaterPriceTier, Long> {
    List<WaterPriceTier> findAllByOrderByMinVolumeAsc();
}
