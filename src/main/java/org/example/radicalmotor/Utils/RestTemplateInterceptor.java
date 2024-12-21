package org.example.radicalmotor.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = getTokenFromCookie();
        if (token != null) {
            request.getHeaders().set("Authorization", "Bearer " + token);
        } else {
            System.out.println("No token found in cookie");
        }

        return execution.execute(request, body);
    }

    private String getTokenFromCookie() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            System.out.println("RequestContextHolder is null");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
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

}
