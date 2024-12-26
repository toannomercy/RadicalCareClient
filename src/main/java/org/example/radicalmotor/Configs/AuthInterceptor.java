package org.example.radicalmotor.Configs;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        boolean isLoggedIn = false;

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is already set and valid
        if (currentAuth == null || currentAuth instanceof AnonymousAuthenticationToken) {
            String token = null;

            // Check token in Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            } else {
                // Fallback to check cookies for token
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("token".equals(cookie.getName())) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
            }

            // Validate token if found
            if (token != null) {
                try {
                    if (jwtUtils.validateToken(token)) {
                        isLoggedIn = true;

                        // Set authentication into SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(token));
                        log.debug("Updated SecurityContextHolder Authentication: {}",
                                SecurityContextHolder.getContext().getAuthentication());
                    } else {
                        clearAuthentication(response);
                    }
                } catch (Exception e) {
                    clearAuthentication(response);
                }
            } else {
                log.info("No token found in request headers or cookies.");
            }
        } else {
            isLoggedIn = true;
        }

        // Attach isLoggedIn attribute for request
        request.setAttribute("isLoggedIn", isLoggedIn);

        return true;
    }

    /**
     * Clear authentication and invalidate session on logout or invalid token.
     *
     * @param response HttpServletResponse
     */
    private void clearAuthentication(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0); // Delete cookie
        cookie.setPath("/"); // Apply to the entire domain
        response.addCookie(cookie);
    }
}