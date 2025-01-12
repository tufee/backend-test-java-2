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

import com.parking.api.responses.VehicleResponse;
import com.parking.domain.entities.Vehicle;
import com.parking.domain.services.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createVehicle(@Valid @RequestBody Vehicle vehicleRequest) {
        service.createVehicle(vehicleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<VehicleResponse> getVehicleById(@Valid @PathVariable Long id) {
        Vehicle vehicle = service.getVehicleById(id);
        return ResponseEntity.ok(VehicleResponse.fromEntity(vehicle));
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<VehicleResponse> updateVehicle(@Valid @PathVariable Long id,
            @RequestBody Vehicle vehicleRequest) {
        Vehicle updatedVehicle = service.updateVehicle(id, vehicleRequest);
        return ResponseEntity.ok(VehicleResponse.fromEntity(updatedVehicle));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteVehicle(@Valid @PathVariable Long id) {
        service.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        List<Vehicle> vehicles = service.getAllVehicles();
        List<VehicleResponse> response = vehicles.stream()
                .map(VehicleResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}
