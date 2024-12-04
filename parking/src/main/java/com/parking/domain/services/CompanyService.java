package com.parking.domain.services;

import org.springframework.stereotype.Service;

import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final AuthService authService;

  public void createCompany(Company company) {
    company.setPassword(authService.passwordToHash(company.getPassword()));
    companyRepository.save(company);
  }
}
