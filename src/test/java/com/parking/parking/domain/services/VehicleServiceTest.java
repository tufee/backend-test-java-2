package com.parking.parking.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.parking.api.exceptions.LicensePlateAlreadyExistsException;
import com.parking.api.exceptions.VehicleNotFoundException;
import com.parking.domain.entities.Vehicle;
import com.parking.domain.repositories.VehicleRepository;
import com.parking.domain.services.AuthService;
import com.parking.domain.services.VehicleService;

public class VehicleServiceTest {
  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private VehicleService vehicleService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  private Vehicle createDefaultVehicle() {
    Vehicle vehicle = new Vehicle();
    vehicle.setId(1L);
    vehicle.setBrand("Toyota");
    vehicle.setModel("Corolla");
    vehicle.setColor("Black");
    vehicle.setLicensePlate("xxxYYY");
    vehicle.setType("Sedan");
    vehicle.setPassword("123");
    return vehicle;
  }

  private Vehicle createDefaultVehicleTwo() {
    Vehicle vehicle = new Vehicle();
    vehicle.setId(2L);
    vehicle.setBrand("Ford");
    vehicle.setModel("F-150");
    vehicle.setColor("Black");
    vehicle.setLicensePlate("dddXXX");
    vehicle.setType("Truck");
    vehicle.setPassword("123");
    return vehicle;
  }

  @Nested
  public class GetAllVehicles {
    @Test
    void testGetAllVehiclesReturnsList() {
      Vehicle vehicle1 = createDefaultVehicle();
      Vehicle vehicle2 = createDefaultVehicleTwo();

      List<Vehicle> mockVehicles = Arrays.asList(vehicle1, vehicle2);

      when(vehicleRepository.findAll()).thenReturn(mockVehicles);

      List<Vehicle> result = vehicleService.getAllVehicles();

      assertEquals(2, result.size());
      assertEquals("Sedan", result.get(0).getType());
      assertEquals("Truck", result.get(1).getType());

      verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void testGetAllVehiclesReturnsEmptyList() {
      when(vehicleRepository.findAll()).thenReturn(List.of());

      List<Vehicle> result = vehicleService.getAllVehicles();

      assertEquals(0, result.size());
      verify(vehicleRepository, times(1)).findAll();
    }
  }

  @Nested
  public class GetVehicleById {
    @Test
    void testGetVehicleByIdSuccess() {
      Long vehicleId = 1L;
      Vehicle vehicle = createDefaultVehicle();
      when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

      Vehicle result = vehicleService.getVehicleById(vehicleId);

      assertNotNull(result);
      assertEquals(vehicleId, result.getId());
      assertEquals("Toyota", result.getBrand());
      verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    void testGetVehicleByIdThrowsExceptionWhenNotFound() {
      Long vehicleId = 1L;
      when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

      VehicleNotFoundException exception = assertThrows(
          VehicleNotFoundException.class,
          () -> vehicleService.getVehicleById(vehicleId));

      assertEquals("Vehicle not found", exception.getMessage());
      verify(vehicleRepository, times(1)).findById(vehicleId);
    }
  }

  @Nested
  public class CreateVehicle {
    @Test
    void testCreateVehicleSuccess() {
      Vehicle vehicle = createDefaultVehicle();

      when(vehicleRepository.findByLicensePlate(vehicle.getLicensePlate())).thenReturn(Optional.empty());
      when(authService.passwordToHash(vehicle.getPassword())).thenReturn("hashedPassword");

      vehicleService.createVehicle(vehicle);

      assertEquals("hashedPassword", vehicle.getPassword());
      verify(vehicleRepository, times(1)).save(vehicle);
      verify(vehicleRepository, times(1)).findByLicensePlate(vehicle.getLicensePlate());
      verify(authService, times(1)).passwordToHash("123");
    }

    @Test
    void testCreateVehicleThrowsExceptionWhenLicensePlateExists() {
      Vehicle vehicle = createDefaultVehicle();
      when(vehicleRepository.findByLicensePlate(vehicle.getLicensePlate()))
          .thenReturn(Optional.of(vehicle));

      LicensePlateAlreadyExistsException exception = assertThrows(
          LicensePlateAlreadyExistsException.class,
          () -> vehicleService.createVehicle(vehicle));

      assertEquals("License Plate already exists", exception.getMessage());
      verify(vehicleRepository, times(0)).save(vehicle);
      verify(authService, times(0)).passwordToHash(anyString());
    }
  }

}
