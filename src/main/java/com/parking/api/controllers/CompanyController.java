package com.parking.api.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.api.responses.GetCompanyResponse;
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

    @GetMapping()
    public ResponseEntity<GetCompanyResponse> findCompany(@Valid @RequestParam String cnpj) {
        Optional<Company> company = companyService.findCompanyByCnpj(cnpj);

        GetCompanyResponse getCompanyResponse = new GetCompanyResponse(
                company.get().getId(),
                company.get().getName(),
                company.get().getCnpj(),
                company.get().getAddress(),
                company.get().getPhone(),
                company.get().getMotorcycleParkingSpace(),
                company.get().getCarParkingSpace(),
                company.get().getCreatedAt(),
                company.get().getUpdatedAt());

        return ResponseEntity.ok(getCompanyResponse);
    }
}
