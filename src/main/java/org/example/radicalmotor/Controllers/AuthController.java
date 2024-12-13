package org.example.radicalmotor.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Dtos.JwtResponse;
import org.example.radicalmotor.Dtos.LoginRequest;
import org.example.radicalmotor.Dtos.RegisterRequest;
import org.example.radicalmotor.Services.AuthApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthApiService authApiService;

    @Autowired
    public AuthController(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    // Hiển thị form login
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, HttpSession session, Model model) {
        try {
            JwtResponse jwtResponse = authApiService.login(loginRequest);

            // Lưu JWT vào cookie
            Cookie jwtCookie = new Cookie("token", jwtResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 1 ngày
            response.addCookie(jwtCookie);

            // Lưu vào session
            session.setAttribute("username", jwtResponse.getFullName());
            session.setAttribute("role", jwtResponse.getRole());

            // Điều hướng dựa trên role
            if ("ADMIN".equals(jwtResponse.getRole())) {
                return "redirect:/admin";
            }
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            // Log thông tin session
            if (session != null) {
                session.invalidate();
                log.info("Session invalidated.");
            }

            // Xóa cookie JWT
            Cookie jwtCookie = new Cookie("token", null);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0);
            response.addCookie(jwtCookie);

            log.info("JWT cookie cleared.");

            return "redirect:/auth/login";
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return "redirect:/auth/login?error=logout_failed";
        }
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute RegisterRequest registerRequest, Model model) {
        try {
            String successMessage = authApiService.register(registerRequest);
            model.addAttribute("successMessage", successMessage);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed.");
        }
        return "auth/register";
    }
}





