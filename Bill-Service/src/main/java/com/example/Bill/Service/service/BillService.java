package com.example.Bill.Service.service;

import com.example.Bill.Service.dto.BillDTO;

import java.util.List;

public interface BillService {
    BillDTO generateBill(Long contractId, Double waterVolume, Long userId);
    List<BillDTO> getBillsByCustomer(Long customerId);
}
