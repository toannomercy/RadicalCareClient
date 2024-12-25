package org.example.radicalmotor.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Declare AuthInterceptor as a dependency
    private final AuthInterceptor authInterceptor;

    /**
     * Constructor injection of AuthInterceptor.
     * Ensure that AuthInterceptor is defined as a Spring Bean.
     */
    @Autowired
    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * Add AuthInterceptor to the Spring MVC Interceptor registry.
     * Applies to all endpoints (/**) with order 0.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // Apply to all paths
                .order(0); // Set the execution order
    }
}