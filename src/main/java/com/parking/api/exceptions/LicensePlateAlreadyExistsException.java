package com.parking.api.exceptions;

public class LicensePlateAlreadyExistsException extends RuntimeException {
  public LicensePlateAlreadyExistsException(String message) {
    super(message);
  }
}
