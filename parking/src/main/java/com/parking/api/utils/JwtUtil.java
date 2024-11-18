package com.parking.api.utils;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
  private static final String SECRET_KEY = "secret";
  private static final long ONE_DAY_IN_MILLIS = 86400000;
  private static final long EXPIRATION_TIME = ONE_DAY_IN_MILLIS;

  public static String generateToken(String cnpj) {
    return Jwts.builder()
        .setSubject(cnpj)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public static String validateToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }
}
