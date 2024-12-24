package com.parking.domain.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public String passwordToHash(String password) {
    return this.bCryptPasswordEncoder.encode(password);
  }

  public Boolean compareHash(String password, String hash) {
      return this.bCryptPasswordEncoder.matches(password, hash);
  }
}