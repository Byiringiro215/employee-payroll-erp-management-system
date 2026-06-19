package com.gov.rw.erp.repository;

import com.gov.rw.erp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmployeeId(Long employeeId);
    List<Message> findByMonthAndYear(Integer month, Integer year);
    Optional<Message> findByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);
}
