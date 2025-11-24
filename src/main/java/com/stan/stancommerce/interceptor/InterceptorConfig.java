package com.stan.stancommerce.interceptor;

import com.stan.stancommerce.repositories.MerchantDetailsRepository;
import com.stan.stancommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final UserService userService;
    private final MerchantDetailsRepository merchantDetailsRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.
           addInterceptor(new LoggingInterceptor(userService, merchantDetailsRepository))
           .addPathPatterns("/carts/1/item");
    }
}
