package com.example.Customer.Service.Repository;

import com.example.Customer.Service.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.fullName = :fullName, c.phone = :phone, c.idCard = :idCard, c.email = :email WHERE c.id = :id")
    int updateCustomer(
            @Param("id") Long id,
            @Param("fullName") String fullName,
            @Param("phone") String phone,
            @Param("idCard") String idCard,
            @Param("email") String email
    );
}
