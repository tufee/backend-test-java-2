package com.parking.api.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.api.requests.AuthRequest;
import com.parking.api.utils.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public String login(@Valid @RequestBody AuthRequest authRequest) {
    try {
      System.out.println("test");
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequest.cnpj(),
              authRequest.password()));
      return JwtUtil.generateToken(authRequest.cnpj());
    } catch (AuthenticationException e) {
      throw new RuntimeException("Invalid credentials");
    }
  }
}
