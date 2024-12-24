package com.parking.api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
    @NotBlank(message = "cnpj is required")
    String cnpj,

    @NotNull(message = "password is required") 
    String password
  ) {
}
