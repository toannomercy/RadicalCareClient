package org.example.radicalmotor.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Slf4j
@Service
public class CartService {

    private final RestTemplate restTemplate;

    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addItemToTemporaryCart(String userId, String chassisNumber, int quantity, String token) {
        // Xây dựng URL với query parameters
        String apiUrl = String.format(
                "http://localhost:8080/api/v1/cart/temporary/add?userId=%s&chassisNumber=%s&quantity=%d",
                userId, chassisNumber, quantity
        );

        try {
            // Tạo HttpEntity chỉ để thêm Authorization header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token); // Đảm bảo header có prefix 'Bearer'
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            log.debug("Sending request to API with URL: {}", apiUrl);
            restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Void.class);

            log.info("Item added to temporary cart successfully. UserId: {}, ChassisNumber: {}, Quantity: {}", userId, chassisNumber, quantity);
        } catch (Exception e) {
            log.error("Error adding item to temporary cart: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Lấy thông tin giỏ hàng tạm thời từ Redis
    public Object getTemporaryCartByUserId(String userId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/temporary/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, Object.class).getBody();
    }

    // Cập nhật sản phẩm trong giỏ hàng tạm thời
    public void updateTemporaryCartItem(Map<String, Object> payload, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/temporary/update";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
        restTemplate.exchange(apiUrl, HttpMethod.PATCH, requestEntity, Void.class);
    }

    // Xóa sản phẩm khỏi giỏ hàng tạm thời
    public void removeItemFromTemporaryCart(String userId, String cartItemId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/temporary/" + userId + "/item/" + cartItemId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, Void.class);
    }

    // Xóa toàn bộ giỏ hàng tạm thời
    public void clearTemporaryCart(String userId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/temporary/cleanup/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Void.class);
    }

    // Thanh toán giỏ hàng: chuyển từ Redis vào database
    public void checkoutCart(String userId, String token) {
        String apiUrl = "http://localhost:8080/api/v1/cart/checkout/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Void.class);
    }
}
