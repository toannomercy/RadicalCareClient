package org.example.radicalmotor.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Component
public class JwtUtils {


    @Value("${jwt.secret}")
    private String secret;

    public Claims parseToken(String token) {
        log.debug("Parsing token...");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.debug("Token parsed successfully. Claims: {}", claims);
            return claims;
        } catch (Exception e) {
            log.error("Error while parsing token: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String getRoleFromToken(String token) {
        log.debug("Extracting role from token...");
        Claims claims = parseToken(token);
        Object authoritiesClaim = claims.get("authorities");

        if (authoritiesClaim instanceof List) {
            List<?> authorities = (List<?>) authoritiesClaim;
            if (!authorities.isEmpty()) {
                String role = authorities.get(0).toString();
                log.debug("Role extracted: {}", role);
                return role;
            }
        }

        log.error("Invalid authorities claim in token: {}", authoritiesClaim);
        throw new IllegalArgumentException("Invalid authorities claim in token: " + authoritiesClaim);
    }

    public boolean validateToken(String token) {
        log.debug("Validating token...");
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);
            log.info("Token validation succeeded.");
            return true;
        } catch (Exception ex) {
            log.warn("Token validation failed: {}", ex.getMessage());
        }
        return false;
    }

    public Claims getClaims(String token) {
        log.debug("Extracting claims from token...");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.debug("Claims extracted: {}", claims);
            return claims;
        } catch (Exception e) {
            log.error("Error while extracting claims from token: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Generate authentication from token
     *
     * @param token JWT token
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        log.debug("Generating authentication from token...");
        Claims claims = getClaims(token);

        String username = claims.getSubject(); // Lấy username từ subject
        List<String> roles = claims.get("authorities", List.class); // Lấy danh sách quyền

        // Tạo danh sách các quyền từ roles
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Tạo đối tượng User từ thông tin
        User user = new User(username, "", authorities);
        log.info("Authentication generated for user: {}", username);

        // Trả về đối tượng Authentication
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}