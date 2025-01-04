package com.parking.api.controllers;

import com.parking.api.requests.AuthRequest;
import com.parking.api.utils.JwtUtil;
import com.parking.domain.entities.Company;
import com.parking.domain.repositories.CompanyRepository;
import com.parking.domain.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CompanyRepository companyRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Optional<Company> company = companyRepository.findByCnpj(authRequest.cnpj());

            if (company.isEmpty()) {
                throw new RuntimeException("cnpj or password is incorrect.");
            }

            Boolean isCorrectPassword = authService.compareHash(authRequest.password(), company.get().getPassword());

            if (!isCorrectPassword) {
                throw new RuntimeException("cnpj or password is incorrect.");
            }

            return JwtUtil.generateToken(authRequest.cnpj());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
