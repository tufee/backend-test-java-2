package com.parking.domain.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  String passwordToHash(String password) {
    return this.bCryptPasswordEncoder.encode(password);
  }

  Boolean compareHash(String password, String hash) {
    Boolean isCorrectPassword = this.bCryptPasswordEncoder.matches(password, hash);
    return isCorrectPassword ? true : false;
  }
}