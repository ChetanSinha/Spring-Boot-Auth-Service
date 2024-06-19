package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String SECRET_KEY = "W4TjJ8qRoNcEjN0IaIlfNqvntJrJ4B8Yp9zzovZ6X1Q=";
  public DecodedJWT validate(String AT) {
    String token = AT;


    try {
      // Create the algorithm for HMAC256
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

      // Create a JWT verifier
      JWTVerifier verifier = JWT.require(algorithm).build();

      // Verify the token and decode it
      return verifier.verify(token);
    } catch (JWTVerificationException | UnsupportedEncodingException exception) {
      throw new RuntimeException("Exception occurred while decoding at: " + AT);
    }
  }


  public String generate(Integer userId) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      long expirationTime = System.currentTimeMillis() + 3600_000; // 1 hour
      String token = JWT.create()
          .withIssuer("DEMO-ISSUER")
          .withSubject("DEMO-SUBJECT")
          .withAudience("DEMO-AUDIENCE")
          .withExpiresAt(new Date(expirationTime))
          .withClaim("userId", userId)
          .sign(algorithm);

      System.out.println("Generated JWT: " + token);
      return token;
    } catch (Exception e) {
      throw new RuntimeException("Exception occurred while generating token");
    }
  }
}