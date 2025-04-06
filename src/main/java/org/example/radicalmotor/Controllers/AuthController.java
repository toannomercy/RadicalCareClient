package org.example.radicalmotor.Controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Dtos.JwtResponse;
import org.example.radicalmotor.Dtos.LoginRequest;
import org.example.radicalmotor.Dtos.RegisterRequest;
import org.example.radicalmotor.Services.AuthApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public String showLoginForm() {
        return "auth/login";
    }

    // Xử lý login
    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            JwtResponse jwtResponse = authApiService.login(loginRequest);

            // Lưu thông tin người dùng vào session
            session.setAttribute("username", jwtResponse.getFullName());
            session.setAttribute("role", jwtResponse.getRole());
            session.setAttribute("userId", jwtResponse.getUserId());

            // Trả token dưới dạng Bearer Token trong response header
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + jwtResponse.getToken())
                    .body("Login successful");
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
    @GetMapping("/oauth2/code/google")
    public String handleGoogleLoginRedirect(@RequestParam("code") String code, HttpSession session, Model model) {
        try {
            // Trao đổi mã code với BE để lấy thông tin người dùng
            JwtResponse jwtResponse = authApiService.googleLogin(code);

            // Lưu thông tin người dùng vào session
            session.setAttribute("username", jwtResponse.getFullName());
            session.setAttribute("role", jwtResponse.getRole());
            session.setAttribute("userId", jwtResponse.getUserId());

            // Chuyển hướng người dùng đến dashboard (FE client)
            return "redirect:/";
        } catch (Exception e) {
            log.error("Google Login failed: {}", e.getMessage());
            model.addAttribute("error", "Google Login failed. Please try again.");
            return "auth/login";
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout(HttpSession session) {
        try {
            // Invalidate session
            if (session != null) {
                session.invalidate();
                log.info("Session invalidated.");
            }
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("fullName") String fullName,
                               @RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("address") String address,
                               @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("doB") String doB,
                               Model model) {
        log.info("Received Registration Request: fullName={}, username={}, email={}, password={}, address={}, phoneNumber={}, doB={}",
                fullName, username, email, password, address, phoneNumber, doB);
        // Validate input
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()||phoneNumber.isEmpty() || password.isEmpty() || address.isEmpty() || doB.isEmpty()) {
            model.addAttribute("errors", List.of("All fields are required."));
            return "auth/register";
        }

        // Tạo đối tượng RegisterRequest
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName(fullName);
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setPhoneNumber(phoneNumber);
        registerRequest.setAddress(address);

        // Parse doB thành LocalDate
        try {
            registerRequest.setDoB(LocalDate.parse(doB));
        } catch (Exception e) {
            model.addAttribute("errors", List.of("Invalid date format for Date of Birth. Use YYYY-MM-DD."));
            return "auth/register";
        }

        // Gọi phương thức register
        try {
            String successMessage = authApiService.register(registerRequest);
            model.addAttribute("message", successMessage);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("errors", List.of("Registration failed. Please try again."));
            log.error("Registration failed: {}", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "auth/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> processForgotPassword(@RequestParam String email) {
        try {
            authApiService.forgotPassword(email);
            return ResponseEntity.ok("Reset link has been sent to your email.");
        } catch (Exception e) {
            log.error("Failed to send reset link: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset link. Please try again.");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
        }

        try {
            authApiService.resetPassword(token, password);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (Exception e) {
            log.error("Failed to reset password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password. Please try again.");
        }
    }
}