package org.example.radicalmotor.Controllers;

import org.example.radicalmotor.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public String home(@CookieValue(name = "token", defaultValue = "") String token, Model model) {
        String role = "";
        if (!token.isEmpty()) {
            role = jwtUtils.getRoleFromToken(token);
        }
        model.addAttribute("role", role);
        return "home/index";
    }
}

