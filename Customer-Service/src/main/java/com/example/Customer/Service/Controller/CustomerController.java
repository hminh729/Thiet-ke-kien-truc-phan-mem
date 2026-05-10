package com.example.Customer.Service.Controller;

import com.example.Customer.Service.Client.AssetClient;
import com.example.Customer.Service.Client.BillClient;
import com.example.Customer.Service.Client.ContractClient;
import com.example.Customer.Service.dto.*;
import com.example.Customer.Service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ContractClient contractClient;
    private final AssetClient assetClient;
    private final BillClient billClient;

    @GetMapping
    public List<CustomerWithContractDTO> getAll() {
        List<CustomerDTO> customers = customerService.getAllCustomers();

        // Gọi Contract-Service lấy tất cả hợp đồng (từ yêu cầu)
        List<ContractDTO> allContracts = getAllContractsFromContractService();

        // Gọi Asset-Service lấy danh sách căn hộ (để trả về kèm thông tin)
        List<ApartmentDTO> allApartments = getAllApartmentsFromAssetService();
        Map<Long, ApartmentDTO> apartmentMap = allApartments.stream()
            .collect(Collectors.toMap(ApartmentDTO::getId, a -> a));

        // Duyệt để xác định với mỗi customer các hợp đồng (Bao gồm cả Active và Terminated)
        Map<Long, List<ContractBriefDTO>> customerContracts = allContracts.stream()
            .map(c -> ContractBriefDTO.builder()
                    .id(c.getId())
                    .customerId(c.getCustomerId())
                    .apartmentId(c.getApartmentId())
                    .status(c.getStatus())
                    .build())
            .collect(Collectors.groupingBy(ContractBriefDTO::getCustomerId));

        return customers.stream()
            .map(customer -> enrichWithContractInfo(customer, customerContracts, apartmentMap))
            .collect(Collectors.toList());
    }

    private List<ContractDTO> getAllContractsFromContractService() {
        try {
            return contractClient.getAllContracts();
        } catch (Exception e) {
            System.err.println("Error calling Contract-Service /all: " + e.getMessage());
            return List.of();
        }
    }

    private List<ApartmentDTO> getAllApartmentsFromAssetService() {
        try {
            return assetClient.getAllApartments();
        } catch (Exception e) {
            System.err.println("Error calling Asset-Service /assets: " + e.getMessage());
            return List.of();
        }
    }

    private CustomerWithContractDTO enrichWithContractInfo(CustomerDTO customer, Map<Long, List<ContractBriefDTO>> customerContracts, Map<Long, ApartmentDTO> apartmentMap) {
        List<ContractBriefDTO> contracts = customerContracts.getOrDefault(customer.getId(), Collections.emptyList());
        List<Long> apartmentIds = contracts.stream().map(ContractBriefDTO::getApartmentId).collect(Collectors.toList());
        List<ApartmentDTO> apartments = apartmentIds.stream()
                .map(apartmentMap::get)
                .filter(a -> a != null)
                .collect(Collectors.toList());

        return CustomerWithContractDTO.builder()
                .customer(customer)
                .apartmentIds(apartmentIds)
                .apartments(apartments)
                .contracts(contracts)
                .isUsingService(!apartmentIds.isEmpty())
                .build();
    }

    @PostMapping
    public CustomerDTO create(@RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/id/{id}")
    public CustomerDTO getById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/phone/{phone}")
    public CustomerWithContractDTO getByPhone(@PathVariable String phone) {
        CustomerDTO customer = customerService.getCustomerByPhone(phone);

        // Gọi Contract-Service lấy tất cả hợp đồng
        List<ContractDTO> allContracts = getAllContractsFromContractService();

        // Gọi Asset-Service lấy danh sách căn hộ
        List<ApartmentDTO> allApartments = getAllApartmentsFromAssetService();
        Map<Long, ApartmentDTO> apartmentMap = allApartments.stream()
            .collect(Collectors.toMap(ApartmentDTO::getId, a -> a));

        // Duyệt để xác định với customer các contract active
        Map<Long, List<ContractBriefDTO>> customerContracts = allContracts.stream()
            .filter(c -> c.getStatus() != null && c.getStatus().equalsIgnoreCase("ACTIVE"))
            .map(c -> ContractBriefDTO.builder()
                    .id(c.getId())
                    .customerId(c.getCustomerId())
                    .apartmentId(c.getApartmentId())
                    .status(c.getStatus())
                    .build())
            .collect(Collectors.groupingBy(ContractBriefDTO::getCustomerId));

        return enrichWithContractInfo(customer, customerContracts, apartmentMap);
    }

    @PutMapping("/{id}")
    public CustomerDTO update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @GetMapping("/statistics/revenue")
    public List<CustomerRevenueDTO> getRevenueStatistics() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return customers.stream().map(customer -> {
            List<BillDTO> bills = List.of();
            try {
                bills = billClient.getBillsByCustomer(customer.getId());
            } catch (Exception e) {
                System.err.println("Error calling Bill-Service for customer " + customer.getId() + ": " + e.getMessage());
            }
            
            Double totalAmount = 0.0;
            Double paidAmount = 0.0;
            Double unpaidAmount = 0.0;

            for (BillDTO b : bills) {
                if (b.getTotalAmount() != null) {
                    totalAmount += b.getTotalAmount();
                    if ("PAID".equalsIgnoreCase(b.getPaymentStatus())) {
                        paidAmount += b.getTotalAmount();
                    } else {
                        unpaidAmount += b.getTotalAmount();
                    }
                }
            }
            
            return CustomerRevenueDTO.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .paidAmount(paidAmount)
                .unpaidAmount(unpaidAmount)
                .build();
        }).collect(Collectors.toList());
    }
}