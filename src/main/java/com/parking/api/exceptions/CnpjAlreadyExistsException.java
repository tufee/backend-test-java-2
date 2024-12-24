package com.parking.api.exceptions;

public class CnpjAlreadyExistsException extends RuntimeException {
  public CnpjAlreadyExistsException(String message) {
    super(message);
  }
}
