package com.parking.domain.services;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.parking.api.exceptions.CnpjAlreadyExistsException;
import com.parking.api.exceptions.CompanyNotFoundException;
import com.parking.api.responses.UpdateCompanyRequest;
import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AuthService authService;

    public void createCompany(Company company) {
        if (companyRepository.findByCnpj(company.getCnpj()).isPresent()) {
            throw new CnpjAlreadyExistsException("Company already exists");
        }

        company.setPassword(authService.passwordToHash(company.getPassword()));
        companyRepository.save(company);
    }

    public Company findCompanyByCnpj(String cnpj) {
        return companyRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

    }

    public Company updateCompany(Long id, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        if (request.name() != null && !request.name().isBlank()) {
            company.setName(request.name());
        }
        if (request.address() != null && !request.address().isBlank()) {
            company.setAddress(request.address());
        }
        if (request.phone() != null && !request.phone().isBlank()) {
            company.setPhone(request.phone());
        }
        if (request.motorcycleParkingSpace() != null && request.motorcycleParkingSpace() >= 0) {
            company.setMotorcycleParkingSpace(request.motorcycleParkingSpace());
        }
        if (request.carParkingSpace() != null && request.carParkingSpace() >= 0) {
            company.setCarParkingSpace(request.carParkingSpace());
        }

        company.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        companyRepository.delete(company);
    }
}
