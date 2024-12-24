package com.parking.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.domain.entities.Company;
import com.parking.domain.services.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping("/create")
  public ResponseEntity<Void> createCompany(@Valid @RequestBody Company company) {
    companyService.createCompany(company);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
