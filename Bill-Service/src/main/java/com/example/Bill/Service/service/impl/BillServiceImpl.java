package com.example.Bill.Service.service.impl;

import com.example.Bill.Service.client.ContractClient;
import com.example.Bill.Service.client.MeterClient;
import com.example.Bill.Service.dto.BillDTO;
import com.example.Bill.Service.dto.ContractDTO;
import com.example.Bill.Service.dto.MeterReadingDTO;
import com.example.Bill.Service.dto.WaterPriceTierDTO;
import com.example.Bill.Service.entity.Bill;
import com.example.Bill.Service.entity.BillPriceTier;
import com.example.Bill.Service.entity.WaterPriceTier;
import com.example.Bill.Service.repository.BillRepository;
import com.example.Bill.Service.repository.WaterPriceTierRepository;
import com.example.Bill.Service.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final ContractClient contractClient;
    private final MeterClient meterClient;
    private final WaterPriceTierRepository priceTierRepository;

    @Override
    public BillDTO generateBill(Long contractId, Double waterVolume, Long userId) {
        ContractDTO contract = contractClient.getContractById(contractId);
        if (contract == null || !"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("Contract not found or not active");
        }

        // Lưu thông tin số nước sang Meter-Service
        MeterReadingDTO readingDTO = new MeterReadingDTO();
        readingDTO.setApartmentId(contract.getApartmentId());
        readingDTO.setReadingValue(waterVolume);
        readingDTO.setReadingDate(LocalDate.now().toString());
        meterClient.addReading(readingDTO);

        // Lấy danh sách bậc giá
        List<WaterPriceTier> tiers = priceTierRepository.findAllByOrderByMinVolumeAsc();
        if (tiers.isEmpty()) {
            tiers = List.of(WaterPriceTier.builder().minVolume(0.0).maxVolume(null).price(10000.0).build());
        }

        Bill newBill = Bill.builder()
                .contractId(contract.getId())
                .customerId(contract.getCustomerId())
                .userId(userId)
                .waterVolume(waterVolume)
                .paymentStatus("UNPAID")
                .billingDate(LocalDate.now())
                .build();

        List<BillPriceTier> billTiers = tiers.stream()
                .filter(t -> t.getMinVolume() < waterVolume)
                .map(t -> BillPriceTier.builder()
                        .bill(newBill)
                        .minVolume(t.getMinVolume())
                        .maxVolume(t.getMaxVolume())
                        .price(t.getPrice())
                        .build())
                .collect(Collectors.toList());
        
        newBill.setPriceTiers(billTiers);

        Bill savedBill = billRepository.save(newBill);
        return convertToDTO(savedBill);
    }

    @Override
    public List<BillDTO> getBillsByCustomer(Long customerId) {
        return billRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        dto.setId(bill.getId());
        dto.setContractId(bill.getContractId());
        dto.setCustomerId(bill.getCustomerId());
        dto.setUserId(bill.getUserId());
        dto.setWaterVolume(bill.getWaterVolume());
        dto.setPaymentStatus(bill.getPaymentStatus());
        dto.setBillingDate(bill.getBillingDate());
        
        List<WaterPriceTierDTO> tierDTOs = bill.getPriceTiers().stream()
                .map(t -> WaterPriceTierDTO.builder()
                        .minVolume(t.getMinVolume())
                        .maxVolume(t.getMaxVolume())
                        .price(t.getPrice())
                        .build())
                .collect(Collectors.toList());
        dto.setPriceTiers(tierDTOs);

        // Tính toán totalAmount động bằng BigDecimal để tránh sai số floating point
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        java.math.BigDecimal remaining = java.math.BigDecimal.valueOf(bill.getWaterVolume());

        for (WaterPriceTierDTO tier : tierDTOs) {
            if (remaining.compareTo(java.math.BigDecimal.ZERO) <= 0) break;

            java.math.BigDecimal tierUsage;
            if (tier.getMaxVolume() == null) {
                tierUsage = remaining;
            } else {
                java.math.BigDecimal tierLimit = java.math.BigDecimal.valueOf(tier.getMaxVolume())
                        .subtract(java.math.BigDecimal.valueOf(tier.getMinVolume()));
                tierUsage = remaining.min(tierLimit);
            }

            total = total.add(tierUsage.multiply(java.math.BigDecimal.valueOf(tier.getPrice())));
            remaining = remaining.subtract(tierUsage);
        }
        dto.setTotalAmount(total.doubleValue());
        
        return dto;
    }
}
