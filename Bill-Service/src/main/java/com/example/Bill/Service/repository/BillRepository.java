package com.example.Bill.Service.repository;

import com.example.Bill.Service.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomerId(Long customerId);
    List<Bill> findByContractId(Long contractId);
    Optional<Bill> findTopByContractIdOrderByBillingDateDesc(Long contractId);
}
