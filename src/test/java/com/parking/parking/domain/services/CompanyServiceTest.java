package com.parking.parking.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.parking.api.exceptions.CnpjAlreadyExistsException;
import com.parking.api.exceptions.CompanyNotFoundException;
import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;
import com.parking.domain.services.AuthService;
import com.parking.domain.services.CompanyService;

public class CompanyServiceTest {
  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private CompanyService companyService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  public class CreateCompany {
    @Test
    public void shouldCreateCompany() {
      Company company = new Company();
      company.setId((long) 1);
      company.setCnpj("63478706000183");
      company.setName("park ltda");
      company.setAddress("Rua x");
      company.setPhone("11988887777");
      company.setMotorcycleParkingSpace(10);
      company.setCarParkingSpace(10);
      company.setPassword("12345");

      when(companyRepository.findByCnpj(company.getCnpj()))
          .thenReturn(Optional.empty());
      when(authService.passwordToHash(company.getPassword()))
          .thenReturn("hashedPassword");

      ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);

      companyService.createCompany(company);

      verify(companyRepository).save(captor.capture());
      Company capturedCompany = captor.getValue();

      assertEquals(1, capturedCompany.getId());
      assertEquals("63478706000183", capturedCompany.getCnpj());
      assertEquals("park ltda", capturedCompany.getName());
      assertEquals("Rua x", capturedCompany.getAddress());
      assertEquals("11988887777", capturedCompany.getPhone());
      assertEquals(10, capturedCompany.getMotorcycleParkingSpace());
      assertEquals(10, capturedCompany.getCarParkingSpace());
      assertEquals("hashedPassword", capturedCompany.getPassword());
    }

    @Test
    public void shouldThrowExceptionWhenCnpjAlreadyExists() {
      Company existingCompany = new Company();
      existingCompany.setId((long) 1);
      existingCompany.setCnpj("63478706000183");
      existingCompany.setName("park ltda");
      existingCompany.setAddress("Rua x");
      existingCompany.setPhone("11988887777");
      existingCompany.setMotorcycleParkingSpace(10);
      existingCompany.setCarParkingSpace(10);
      existingCompany.setPassword("12345");

      when(companyRepository.findByCnpj(existingCompany.getCnpj()))
          .thenReturn(Optional.of(existingCompany));

      Company newCompany = new Company();
      newCompany.setId((long) 1);
      newCompany.setCnpj("63478706000183");
      newCompany.setName("park ltda");
      newCompany.setAddress("Rua x");
      newCompany.setPhone("11988887777");
      newCompany.setMotorcycleParkingSpace(10);
      newCompany.setCarParkingSpace(10);
      newCompany.setPassword("12345");

      CnpjAlreadyExistsException exception = assertThrows(CnpjAlreadyExistsException.class, () -> {
        companyService.createCompany(newCompany);
      });

      assertEquals("Company already exists", exception.getMessage());
      verify(companyRepository, never()).save(any());
    }
  }

  @Nested
  public class FindCompanyByCnpj {
    @Test
    public void shouldFindCompany() {
      var cnpj = "63478706000183";

      Company existingCompany = new Company();
      existingCompany.setId((long) 1);
      existingCompany.setCnpj(cnpj);
      existingCompany.setName("park ltda");
      existingCompany.setAddress("Rua x");
      existingCompany.setPhone("11988887777");
      existingCompany.setMotorcycleParkingSpace(10);
      existingCompany.setCarParkingSpace(10);
      existingCompany.setPassword("12345");

      when(companyRepository.findByCnpj(cnpj))
          .thenReturn(Optional.of(existingCompany));

      Company company = companyService.findCompanyByCnpj(cnpj);
      assertEquals(existingCompany, company);
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNotFound() {
      var cnpj = "63478706000183";

      when(companyRepository.findByCnpj(cnpj))
          .thenReturn(Optional.empty());

      CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class,
          () -> companyService.findCompanyByCnpj(cnpj));

      assertEquals("Company not found", exception.getMessage());
    }
  }
}