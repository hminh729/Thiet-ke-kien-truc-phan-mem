package com.example.Bill.Service.config;

import com.example.Bill.Service.entity.WaterPriceTier;
import com.example.Bill.Service.repository.WaterPriceTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final WaterPriceTierRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                WaterPriceTier.builder().minVolume(0.0).maxVolume(10.0).price(5973.0).build(),
                WaterPriceTier.builder().minVolume(10.0).maxVolume(20.0).price(7052.0).build(),
                WaterPriceTier.builder().minVolume(20.0).maxVolume(30.0).price(8669.0).build(),
                WaterPriceTier.builder().minVolume(30.0).maxVolume(null).price(15929.0).build()
            ));
            System.out.println("Seeded default water price tiers.");
        }
    }
}
