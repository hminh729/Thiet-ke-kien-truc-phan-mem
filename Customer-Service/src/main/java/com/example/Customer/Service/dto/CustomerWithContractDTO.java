package com.example.Customer.Service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerWithContractDTO {
    private CustomerDTO customer;
    private List<Long> apartmentIds; // các phòng đang thuê
    private List<ApartmentDTO> apartments; // thông tin chi tiết căn hộ
    private List<ContractBriefDTO> contracts; // hợp đồng active của khách hàng
    private boolean isUsingService; // có đang sử dụng dịch vụ thuê nước sạch hay không
}