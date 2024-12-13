package org.example.radicalmotor.Utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        Object authoritiesClaim = claims.get("authorities");

        if (authoritiesClaim instanceof List) {
            // Nếu "authorities" là danh sách, lấy vai trò đầu tiên
            List<?> authorities = (List<?>) authoritiesClaim;
            if (!authorities.isEmpty()) {
                return authorities.get(0).toString(); // Lấy vai trò đầu tiên
            }
        }

        throw new IllegalArgumentException("Invalid authorities claim in token: " + authoritiesClaim);
    }

    /**
     * Validate the provided JWT token
     *
     * @param token JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            // Parse the token to check its validity
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes()) // Use the same signing key as when creating the token
                    .build()
                    .parseClaimsJws(token);
            return true; // Token is valid
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false; // Token is invalid
    }

    /**
     * Get claims from token
     *
     * @param token JWT token
     * @return Claims extracted from the token
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes()) // Use the same signing key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
