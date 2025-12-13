package com.fasttasker.fast_tasker.config;
// creado con Gemini

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Genera un nuevo token JWT para un ID de cuenta.
     */
    public String generateToken(UUID accountId, UUID taskerId, boolean completedProfile) {
        return Jwts.builder()
                .subject(accountId.toString()) // ¡Aquí guardamos el ID!
                .claim("taskerId", taskerId)
                .claim("profileCompleted", completedProfile) // claim = extra information
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Valida un token (comprueba firma y expiración).
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el ID de la cuenta (el "subject") del token.
     */
    public UUID extractAccountId(String token) {
        String id = extractClaim(token, Claims::getSubject);
        return UUID.fromString(id);
    }

    public UUID extractTaskerId(Authentication authentication) {
        log.info("call extractTaskerId");
        log.info("authentication: {}", authentication);
        if (authentication == null) {
            log.info("authentication is null");
            return null;
        }

        // NOTE: refactor
        Object details = authentication.getDetails();
        var claims = (Claims) details;
        String taskerIdStr = claims.get("taskerId", String.class);
        log.info("taskerId: {}", taskerIdStr);
        return UUID.fromString(taskerIdStr);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
