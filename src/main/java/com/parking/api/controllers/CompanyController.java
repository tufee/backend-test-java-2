package com.parking.api.controllers;

import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;
import com.parking.domain.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @PostMapping("/create")
    public ResponseEntity<Void> createCompany(@Valid @RequestBody Company company) {
        companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping()
//    public ResponseEntity<Optional<Company>> findCompany(@Valid @RequestBody String cnpj) {
//        Optional<Company> company = companyRepository.findByCnpj(cnpj);
//
//        if (company.isEmpty()) {
//            throw new RuntimeException("Company not found");
//        }
//        return ResponseEntity.status(HttpStatus.FOUND).body(company);
//    }
}
