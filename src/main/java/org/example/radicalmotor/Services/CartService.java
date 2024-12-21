package org.example.radicalmotor.Services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.radicalmotor.Daos.Item;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CartService {

    private final RestTemplate restTemplate;

    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public void addItemToCart(Item item, HttpServletRequest request) {
        String apiUrl = "http://localhost:8080/api/v1/cart/add";

        // Tạo header với token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getTokenFromCookie(request));

        // Gửi request đến backend
        HttpEntity<Item> httpEntity = new HttpEntity<>(item, headers);
        restTemplate.postForObject(apiUrl, httpEntity, Void.class);
    }


    // Phương thức lấy token từ cookie
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    System.out.println("Token found in cookie: " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        System.out.println("Token not found in cookie");
        return null;
    }


    public Object getCartByUserId(String userId) {
        String apiUrl = "http://localhost:8080/api/v1/cart/" + userId;
        return restTemplate.getForObject(apiUrl, Object.class);
    }


    public void updateCartItem(Object cartItem) {
        String apiUrl = "http://localhost:8080/api/v1/cart/update";
        restTemplate.put(apiUrl, cartItem);
    }

    public void removeItemFromCart(String itemId) {
        String apiUrl = "http://localhost:8080/api/v1/cart/remove/" + itemId;
        restTemplate.delete(apiUrl);
    }

    public void clearCart(String userId) {
        String apiUrl = "http://localhost:8080/api/v1/cart/clear/" + userId;
        restTemplate.delete(apiUrl);
    }
}

