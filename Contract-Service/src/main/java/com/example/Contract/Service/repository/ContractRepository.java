package com.example.Contract.Service.repository;

import com.example.Contract.Service.Entity.Contract;
import com.example.Contract.Service.Entity.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByCustomerId(Long customerId);

    List<Contract> findByStatus(ContractStatus status);
}