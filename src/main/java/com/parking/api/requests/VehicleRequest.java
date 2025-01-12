package com.parking.api.requests;

import jakarta.validation.constraints.NotBlank;

public record VehicleRequest(
    @NotBlank(message = "Brand is required")
    String brand,

    @NotBlank(message = "Model is required")
    String model,

    @NotBlank(message = "Color is required")
    String color,

    @NotBlank(message = "License plate is required")
    String licensePlate,

    @NotBlank(message = "Type is required")
    String type) {
}