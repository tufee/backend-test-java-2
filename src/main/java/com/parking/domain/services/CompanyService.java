package com.parking.domain.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.parking.api.exceptions.CnpjAlreadyExistsException;
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
            throw new CnpjAlreadyExistsException("Company already exists.");
        }

        company.setPassword(authService.passwordToHash(company.getPassword()));
        companyRepository.save(company);
    }

    public Optional<Company> findCompanyByCnpj(String cnpj) {
        Optional<Company> company = companyRepository.findByCnpj(cnpj);

        if (company.isEmpty()) {
            throw new RuntimeException("Company not found");
        }

        return company;
    }
}
