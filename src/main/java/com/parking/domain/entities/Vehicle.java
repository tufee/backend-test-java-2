package com.parking.domain.entities;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String brand;

  @Column(nullable = false)
  private String model;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false, unique = true)
  private String licensePlate;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, name = "created_at", updatable = false)
  private final OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

  @Column(nullable = true, name = "updated_at")
  private OffsetDateTime updatedAt;
}