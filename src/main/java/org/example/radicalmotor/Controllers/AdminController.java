package org.example.radicalmotor.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/index")
    public String adminIndex(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);
        if (token == null) {
            log.warn("No token found in request.");
            return "redirect:/auth/login";
        }
        log.info("Token: {}", token);

        if (!jwtUtils.validateToken(token)) {
            log.warn("Invalid token.");
            return "redirect:/auth/login";
        }

        String role = jwtUtils.getRoleFromToken(token);
        log.info("Role extracted: {}", role);

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            log.warn("Unauthorized role: {}", role);
            return "redirect:/auth/login";
        }

        return "admin/index";
    }


    private String extractTokenFromCookies(HttpServletRequest request) {
        // Kiểm tra các cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) { // Tên cookie chứa token
                    return cookie.getValue();
                }
            }
        }
        return null; // Không tìm thấy token
    }
}
