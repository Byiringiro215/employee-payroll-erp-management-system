package com.gov.rw.erp.repository;

import com.gov.rw.erp.entity.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    Optional<Deduction> findByDeductionNameIgnoreCase(String deductionName);
    boolean existsByDeductionNameIgnoreCase(String deductionName);
}
