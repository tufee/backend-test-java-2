package com.parking.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.api.exceptions.LicensePlateAlreadyExistsException;
import com.parking.api.exceptions.VehicleNotFoundException;
import com.parking.domain.entities.Vehicle;
import com.parking.domain.repositories.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

  private final VehicleRepository repository;
  private final AuthService authService;

  public List<Vehicle> getAllVehicles() {
    return repository.findAll();
  }

  public Vehicle getVehicleById(Long id) {
    return repository.findById(id).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
  }

  @Transactional
  public void createVehicle(Vehicle vehicle) {
    if (repository.findByLicensePlate(vehicle.getLicensePlate()).isPresent()) {
      throw new LicensePlateAlreadyExistsException("License Plate already exists");
    }

    vehicle.setPassword(authService.passwordToHash(vehicle.getPassword()));
    repository.save(vehicle);
  }

  public Vehicle updateVehicle(Long id, Vehicle request) {
    Vehicle vehicle = getVehicleById(id);

    if (request.getBrand() != null) {
      vehicle.setBrand(request.getBrand());
    }

    if (request.getModel() != null) {
      vehicle.setModel(request.getModel());
    }

    if (request.getColor() != null) {
      vehicle.setColor(request.getColor());
    }

    if (request.getLicensePlate() != null) {
      vehicle.setLicensePlate(request.getLicensePlate());
    }

    if (request.getType() != null) {
      vehicle.setType(request.getType());
    }

    return repository.save(vehicle);
  }

  @Transactional
  public void deleteVehicle(Long id) {
    repository.deleteById(id);
  }
}