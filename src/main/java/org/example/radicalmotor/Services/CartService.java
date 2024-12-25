package org.example.radicalmotor.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CartService {

    private final RestTemplate restTemplate;

    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addItemToCart(Map<String, Object> payload, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/add";

        // Tạo header với token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Gửi payload tới backend
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(payload, headers);
        restTemplate.postForObject(apiUrl, httpEntity, Void.class);
    }

    public Object getCartByUserId(String userId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.getForObject(apiUrl, Object.class, requestEntity);
    }

    public void updateCartItem(Object cartItem, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/update";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(cartItem, headers);
        restTemplate.put(apiUrl, requestEntity);
    }

    public void removeItemFromCart(String itemId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/remove/" + itemId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, Void.class);
    }

    public void clearCart(String userId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/clear/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, Void.class);
    }
}