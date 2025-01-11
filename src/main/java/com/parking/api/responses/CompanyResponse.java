package com.parking.api.responses;

import java.time.OffsetDateTime;

import com.parking.domain.entities.Company;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "company")
public record CompanyResponse(
                Long id,
                String name,
                String cnpj,
                String address,
                String phone,
                Integer motorcycleParkingSpace,
                Integer carParkingSpace,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {

        public static CompanyResponse fromEntity(Company company) {
                return new CompanyResponse(
                                company.getId(),
                                company.getName(),
                                company.getCnpj(),
                                company.getAddress(),
                                company.getPhone(),
                                company.getMotorcycleParkingSpace(),
                                company.getCarParkingSpace(),
                                company.getCreatedAt(),
                                company.getUpdatedAt());
        }
}
