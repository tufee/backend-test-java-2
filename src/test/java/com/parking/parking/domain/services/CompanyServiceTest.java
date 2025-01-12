package com.parking.parking.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import com.parking.api.requests.UpdateCompanyRequest;
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

  private Company createDefaultCompany() {
    Company company = new Company();
    company.setId(1L);
    company.setCnpj("63478706000183");
    company.setName("park ltda");
    company.setAddress("Rua x");
    company.setPhone("11988887777");
    company.setMotorcycleParkingSpace(10);
    company.setCarParkingSpace(10);
    company.setPassword("12345");
    return company;
  }

  @Nested
  public class CreateCompany {
    @Test
    public void shouldCreateCompany() {
      Company company = createDefaultCompany();
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
      Company existingCompany = createDefaultCompany();

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

      Company existingCompany = createDefaultCompany();

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

  @Nested
  public class UpdateCompany {
    @Test
    void shouldUpdateOnlyNonNullFields() {
      Long companyId = 1L;
      Company existingCompany = createDefaultCompany();

      UpdateCompanyRequest request = new UpdateCompanyRequest(
          "new name",
          null,
          "",
          null,
          25);

      when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
      when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArguments()[0]);

      Company updatedCompany = companyService.updateCompany(companyId, request);

      assertEquals("new name", updatedCompany.getName());
      assertEquals("Rua x", updatedCompany.getAddress());
      assertEquals("11988887777", updatedCompany.getPhone());
      assertEquals(10, updatedCompany.getMotorcycleParkingSpace());
      assertEquals(25, updatedCompany.getCarParkingSpace());
      assertNotNull(updatedCompany.getUpdatedAt());
    }

    @Test
    void shouldNotUpdateWithEmptyStrings() {
      Long companyId = 1L;
      Company existingCompany = createDefaultCompany();

      UpdateCompanyRequest request = new UpdateCompanyRequest(
          "",
          "   ",
          null,
          null,
          null);

      when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
      when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArguments()[0]);

      Company updatedCompany = companyService.updateCompany(companyId, request);

      assertEquals("park ltda", updatedCompany.getName());
      assertEquals("Rua x", updatedCompany.getAddress());
      assertEquals("11988887777", updatedCompany.getPhone());
      assertEquals(10, updatedCompany.getMotorcycleParkingSpace());
      assertEquals(10, updatedCompany.getCarParkingSpace());
    }

    @Test
    void shouldNotUpdateWithNegativeValues() {
      Long companyId = 1L;
      Company existingCompany = createDefaultCompany();

      UpdateCompanyRequest request = new UpdateCompanyRequest(
          null,
          null,
          null,
          -5,
          -10);

      when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
      when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArguments()[0]);

      Company updatedCompany = companyService.updateCompany(companyId, request);

      assertEquals(10, updatedCompany.getMotorcycleParkingSpace());
      assertEquals(10, updatedCompany.getCarParkingSpace());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
      Long companyId = 1L;
      UpdateCompanyRequest request = new UpdateCompanyRequest(
          "new name",
          "Rua x",
          "11988887777",
          15,
          25);

      when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

      CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class,
          () -> companyService.updateCompany(companyId, request));

      assertEquals("Company not found", exception.getMessage());
      verify(companyRepository).findById(companyId);
      verify(companyRepository, never()).save(any());
    }
  }

  @Nested
  public class DeleteCompany {
    @Test
    void shouldDeleteCompanySuccessfully() {
      Long companyId = 1L;
      Company company = new Company();
      company.setId(companyId);

      when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
      doNothing().when(companyRepository).delete(company);

      assertDoesNotThrow(() -> companyService.deleteCompany(companyId));

      verify(companyRepository).findById(companyId);
      verify(companyRepository).delete(company);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCompany() {
      Long companyId = 1L;
      when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

      CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class,
          () -> companyService.deleteCompany(companyId));

      assertEquals("Company not found", exception.getMessage());
      verify(companyRepository).findById(companyId);
      verify(companyRepository, never()).delete(any());
    }
  }

  @Nested
  public class GetAllCompanies {
    private Company createCompany(Long id, String name) {
      Company company = new Company();
      company.setId(id);
      company.setName(name);
      return company;
    }

    @Test
    void shouldReturnAllCompanies() {
      List<Company> companies = Arrays.asList(
          this.createCompany(1L, "Company A"),
          createCompany(2L, "Company B"));

      when(companyRepository.findAll()).thenReturn(companies);

      List<Company> result = companyService.getAllCompanies();

      assertNotNull(result);
      assertEquals(2, result.size());
      assertEquals("Company A", result.get(0).getName());
      assertEquals("Company B", result.get(1).getName());

      verify(companyRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCompanies() {
      when(companyRepository.findAll()).thenReturn(Collections.emptyList());

      List<Company> result = companyService.getAllCompanies();

      assertTrue(result.isEmpty());
      verify(companyRepository).findAll();
    }

  }
}