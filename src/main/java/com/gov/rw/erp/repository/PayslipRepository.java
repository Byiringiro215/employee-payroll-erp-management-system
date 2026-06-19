package com.gov.rw.erp.repository;

import com.gov.rw.erp.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    List<Payslip> findByMonthAndYear(Integer month, Integer year);

    List<Payslip> findByEmployeeId(Long employeeId);

    Optional<Payslip> findByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);

    boolean existsByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);
}
