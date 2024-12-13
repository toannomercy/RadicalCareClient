package org.example.radicalmotor.Utils;

import jakarta.servlet.http.Cookie;
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
            request.getHeaders().set("Authorization", "Bearer " + token); // Thêm token vào header
        }
        return execution.execute(request, body); // Gửi request tiếp
    }

    // Lấy token từ cookie
    private String getTokenFromCookie() {
        return Arrays.stream(Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                        .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes).getRequest())
                        .map(request -> request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> "token".equals(cookie.getName())) // Tìm cookie có tên "token"
                .map(Cookie::getValue) // Lấy giá trị của cookie
                .findFirst()
                .orElse(null); // Nếu không có cookie, trả về null
    }
}
