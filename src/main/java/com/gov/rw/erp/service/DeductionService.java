package com.gov.rw.erp.service;

import com.gov.rw.erp.dto.DeductionRequest;
import com.gov.rw.erp.entity.Deduction;
import com.gov.rw.erp.repository.DeductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeductionService {

    private final DeductionRepository deductionRepository;

    public Deduction create(DeductionRequest request) {
        if (deductionRepository.existsByDeductionNameIgnoreCase(request.getDeductionName())) {
            throw new IllegalArgumentException("Deduction already exists: " + request.getDeductionName());
        }
        Deduction deduction = Deduction.builder()
                .deductionName(request.getDeductionName())
                .percentage(request.getPercentage())
                .build();
        return deductionRepository.save(deduction);
    }

    public Deduction update(Long id, DeductionRequest request) {
        Deduction deduction = findById(id);
        deduction.setDeductionName(request.getDeductionName());
        deduction.setPercentage(request.getPercentage());
        return deductionRepository.save(deduction);
    }

    public List<Deduction> getAll() {
        return deductionRepository.findAll();
    }

    public Deduction findById(Long id) {
        return deductionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deduction not found: " + id));
    }

    public void delete(Long id) {
        findById(id);
        deductionRepository.deleteById(id);
    }

    // Helpers to get specific deduction percentages by name
    public double getPercentage(String name) {
        return deductionRepository.findByDeductionNameIgnoreCase(name)
                .map(Deduction::getPercentage)
                .orElse(0.0);
    }
}
