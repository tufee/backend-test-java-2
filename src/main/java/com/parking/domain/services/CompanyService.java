package com.parking.domain.services;

import org.springframework.stereotype.Service;

import com.parking.api.exceptions.CnpjAlreadyExistsException;
import com.parking.api.exceptions.CompanyNotFoundException;
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
}
