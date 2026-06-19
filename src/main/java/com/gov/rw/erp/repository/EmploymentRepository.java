package com.gov.rw.erp.repository;

import com.gov.rw.erp.entity.Employment;
import com.gov.rw.erp.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    Optional<Employment> findByEmployeeId(Long employeeId);
    List<Employment> findByStatus(EmployeeStatus status);
    boolean existsByEmployeeCode(String employeeCode);
}
