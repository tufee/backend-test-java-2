package com.parking.api.responses;

import java.time.OffsetDateTime;

import com.parking.domain.entities.Vehicle;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicle")
public record VehicleResponse(
                Long id,
                String brand,
                String model,
                String color,
                String licensePlate,
                String type,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {

        public static VehicleResponse fromEntity(Vehicle vehicle) {
                return new VehicleResponse(
                                vehicle.getId(),
                                vehicle.getBrand(),
                                vehicle.getModel(),
                                vehicle.getColor(),
                                vehicle.getLicensePlate(),
                                vehicle.getType(),
                                vehicle.getCreatedAt(),
                                vehicle.getUpdatedAt());

        }
}
