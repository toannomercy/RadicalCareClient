package org.example.radicalmotor.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Dtos.JwtResponse;
import org.example.radicalmotor.Dtos.LoginRequest;
import org.example.radicalmotor.Dtos.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    public void forgotPassword(String email) {
        String url = baseUrl + "/api/v1/auth/forgot-password";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);

        restTemplate.postForObject(url, params, String.class);
    }


    public void resetPassword(String token, String password) {
        String url = baseUrl + "/api/v1/auth/reset-password";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", token);
        requestBody.put("password", password);

        try {
            restTemplate.postForEntity(url, requestBody, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset password: " + e.getMessage());
        }
    }
}




