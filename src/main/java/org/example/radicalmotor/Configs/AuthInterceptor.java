package org.example.radicalmotor.Configs;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.radicalmotor.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        boolean isLoggedIn = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Log thông tin cookie
                logger.info("Cookie Name: {}, Cookie Value: {}", cookie.getName(), cookie.getValue());

                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtUtils.validateToken(token)) {
                        isLoggedIn = true;
                        break;
                    }
                }
            }
        } else {
            logger.info("No cookies found in the request.");
        }

        // Thêm thông tin đăng nhập vào request attribute để Thymeleaf sử dụng
        request.setAttribute("isLoggedIn", isLoggedIn);

        return true;
    }
}


