package org.example.radicalmotor.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Daos.Item;
import org.example.radicalmotor.Services.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

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
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/add")
    public String addItemToCart(@RequestParam String chassisNumber,
                                @RequestParam String vehicleName,
                                @RequestParam String color,
                                @RequestParam String price,
                                @RequestParam String userId,
                                @RequestParam Integer quantity,
                                HttpServletRequest request) {
        try {
            log.info("Received data: chassisNumber={}, vehicleName={}, color={}, price={}, userId={}, quantity={}",
                    chassisNumber, vehicleName, color, price, userId, quantity);

            Double parsedPrice = parseCurrency(price);

            // Tạo payload tương thích với record backend
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", UUID.randomUUID().toString());
            Map<String, Object> vehicle = new HashMap<>();
            vehicle.put("chassisNumber", chassisNumber);
            vehicle.put("vehicleName", vehicleName);
            vehicle.put("color", color);
            vehicle.put("price", parsedPrice);
            payload.put("vehicle", vehicle);
            payload.put("userId", userId);
            payload.put("quantity", quantity);
            payload.put("price", parsedPrice);
            payload.put("subtotal", parsedPrice * quantity);

            // Gửi payload tới backend qua Service
            cartService.addItemToCart(payload, request);
            return "vehicle/cart";
        } catch (Exception e) {
            log.error("Error adding item to cart", e);
            return "redirect:/error";
        }
    }
}