package com.example.Bill.Service.controller;

import com.example.Bill.Service.dto.BillDTO;
import com.example.Bill.Service.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/generate")
    public BillDTO generateBill(@RequestParam Long contractId, @RequestParam Double waterVolume, @RequestParam Long userId) {
        return billService.generateBill(contractId, waterVolume, userId);
    }

    @GetMapping("/customer/{customerId}")
    public List<BillDTO> getBillsByCustomer(@PathVariable Long customerId) {
        return billService.getBillsByCustomer(customerId);
    }
}
