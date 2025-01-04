package com.parking.domain.services;

import com.parking.api.exceptions.CnpjAlreadyExistsException;
import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
