package com.parking.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.api.responses.GetCompanyResponse;
import com.parking.api.responses.UpdateCompanyRequest;
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
        Company company = companyService.findCompanyByCnpj(cnpj);

        GetCompanyResponse getCompanyResponse = new GetCompanyResponse(
                company.getId(),
                company.getName(),
                company.getCnpj(),
                company.getAddress(),
                company.getPhone(),
                company.getMotorcycleParkingSpace(),
                company.getCarParkingSpace(),
                company.getCreatedAt(),
                company.getUpdatedAt());

        return ResponseEntity.ok(getCompanyResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetCompanyResponse> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyRequest updateCompanyRequest) {

        Company updatedCompany = companyService.updateCompany(id, updateCompanyRequest);

        GetCompanyResponse response = new GetCompanyResponse(
                updatedCompany.getId(),
                updatedCompany.getName(),
                updatedCompany.getCnpj(),
                updatedCompany.getAddress(),
                updatedCompany.getPhone(),
                updatedCompany.getMotorcycleParkingSpace(),
                updatedCompany.getCarParkingSpace(),
                updatedCompany.getCreatedAt(),
                updatedCompany.getUpdatedAt());

        return ResponseEntity.ok(response);
    }
}
