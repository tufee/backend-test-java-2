package com.parking.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.domain.entities.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  Optional<Company> findByCnpj(String cnpj);
}