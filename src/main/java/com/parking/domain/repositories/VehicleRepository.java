package com.parking.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parking.domain.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
  Optional<Vehicle> findByLicensePlate(String licensePlate);
}