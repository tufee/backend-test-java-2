package com.parking.domain.services;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.parking.api.requests.AuthRequest;
import com.parking.api.utils.JwtUtil;
import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final CompanyRepository companyRepository;
  private final JwtUtil jwtUtil;

  public String passwordToHash(String password) {
    return this.bCryptPasswordEncoder.encode(password);
  }

  public Boolean compareHash(String password, String hash) {
    return this.bCryptPasswordEncoder.matches(password, hash);
  }

  public String checkLogin(AuthRequest authRequest) {
    Optional<Company> company = companyRepository.findByCnpj(authRequest.cnpj());

    if (company.isEmpty()) {
      throw new RuntimeException("cnpj or password is incorrect.");
    }

    Boolean isCorrectPassword = this.compareHash(authRequest.password(), company.get().getPassword());

    if (!isCorrectPassword) {
      throw new RuntimeException("cnpj or password is incorrect.");
    }

    return jwtUtil.generateToken(authRequest.cnpj());
  }
}