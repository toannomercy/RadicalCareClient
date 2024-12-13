package org.example.radicalmotor.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Dtos.JwtResponse;
import org.example.radicalmotor.Dtos.LoginRequest;
import org.example.radicalmotor.Dtos.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AuthApiService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String baseUrl;

    @Autowired
    public AuthApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JwtResponse login(LoginRequest loginRequest) {
        String url = baseUrl + "/api/v1/auth/login";
        return restTemplate.postForObject(url, loginRequest, JwtResponse.class);
    }

    public void logout() {
        String url = baseUrl + "/api/v1/auth/logout";
        restTemplate.postForObject(url, null, Void.class);
    }

    public String register(RegisterRequest registerRequest) {
        return restTemplate.postForObject(baseUrl + "/api/v1/auth/register", registerRequest, String.class);
    }

    public String forgotPassword(String email) {
        return restTemplate.postForObject(baseUrl + "/api/v1/auth/forgot-password", email, String.class);
    }

    public String resetPassword(String token, String newPassword) {
        return restTemplate.postForObject(baseUrl + "/api/v1/auth/reset-password?token=" + token, newPassword, String.class);
    }
}




