package com.parking.api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("9fjjZ6HuzwB4QGHFLqKi4vq8RGkAPU2fHdskaHKuwu223hhsaHdh".getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos

    public static String generateToken(String cnpj) {
        return Jwts.builder()
                .subject(cnpj)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}
