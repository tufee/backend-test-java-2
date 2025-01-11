package com.parking.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.api.responses.CompanyResponse;
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

    @GetMapping(value = "/{cnpj}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CompanyResponse> findCompany(@Valid @PathVariable String cnpj) {
        Company company = companyService.findCompanyByCnpj(cnpj);
        return ResponseEntity.ok(CompanyResponse.fromEntity(company));
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyRequest updateCompanyRequest) {

        Company updatedCompany = companyService.updateCompany(id, updateCompanyRequest);
        return ResponseEntity.ok(CompanyResponse.fromEntity(updatedCompany));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyResponse> response = companies.stream()
                .map(CompanyResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}
