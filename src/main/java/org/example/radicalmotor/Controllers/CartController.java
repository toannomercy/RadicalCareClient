package org.example.radicalmotor.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Services.CartService;
import org.example.radicalmotor.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    @Autowired
    private JwtUtils jwtUtils;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    public Double parseCurrency(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        String sanitizedInput = input.replaceAll("[^\\d.]", "");
        return Double.parseDouble(sanitizedInput);
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addItemToCart(@RequestParam String chassisNumber,
                                             @RequestParam String vehicleName,
                                             @RequestParam String color,
                                             @RequestParam String price,
                                             @RequestParam Integer quantity,
                                             HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("Request received to add item to cart: chassisNumber={}, vehicleName={}, color={}, price={}, quantity={}",
                    chassisNumber, vehicleName, color, price, quantity);

            // Lấy token từ cookie
            String token = extractToken(request);
            if (token == null || token.isEmpty()) {
                log.error("Token not found.");
                response.put("status", "error");
                response.put("message", "Bạn cần đăng nhập để thực hiện thao tác này.");
                return response;
            }

            log.info("Token extracted successfully: {}", token);

            // Lấy userId từ Authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = jwtUtils.getUserIdFromToken(token);

            log.info("Authenticated userId: {}", userId);

            // Parse price
            Double parsedPrice = parseCurrency(price);
            log.info("Parsed price: {}", parsedPrice);

            // Gọi service để thêm sản phẩm
            cartService.addItemToTemporaryCart(userId, chassisNumber, quantity, token);

            response.put("status", "success");
            response.put("message", "Thêm sản phẩm vào giỏ hàng thành công.");
        } catch (Exception e) {
            log.error("Error adding item to cart.", e);
            response.put("status", "error");
            response.put("message", "Đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng.");
        }
        return response;
    }

    private String extractToken(HttpServletRequest request) {
        // Ưu tiên lấy token từ header
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            log.debug("Found token in header: {}", token);
            return token;
        }

        // Nếu không có trong header, kiểm tra cookie
        log.info("Token not found in header. Checking cookies...");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.debug("Checking cookie: {}", cookie.getName());
                if ("token".equals(cookie.getName())) {
                    log.debug("Found token in cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        log.warn("No token found in header or cookies.");
        return null;
    }
}