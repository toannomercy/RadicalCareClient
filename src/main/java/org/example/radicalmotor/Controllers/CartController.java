package org.example.radicalmotor.Controllers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Daos.Item;
import org.example.radicalmotor.Services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
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

    @PostMapping("/add")
    public String addItemToCart(@RequestParam String chassisNumber,
                                @RequestParam String vehicleName,
                                @RequestParam String price,
                                @RequestParam int quantity,
                                HttpServletRequest request) {
        try {
            log.info("Received data: chassisNumber={}, vehicleName={}, price={}, quantity={}",
                    chassisNumber, vehicleName, price, quantity);
            Double parsedPrice = parseCurrency(price);
            Item item = new Item(chassisNumber, vehicleName, parsedPrice, quantity);
            cartService.addItemToCart(item, request);
            return "vehicle/cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

}

