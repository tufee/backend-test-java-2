package com.parking.domain.services;

import org.springframework.stereotype.Service;

import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  public void createCompany(Company company) {
    companyRepository.save(company);
  }
}
