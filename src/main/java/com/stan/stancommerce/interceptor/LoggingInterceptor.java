package com.stan.stancommerce.interceptor;

import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.entities.MerchantDetails;
import com.stan.stancommerce.repositories.MerchantDetailsRepository;
import com.stan.stancommerce.service.UserService;
import com.stan.stancommerce.service.security.authservice.JwtService;
import com.stan.stancommerce.util.RemoteIpHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final MerchantDetailsRepository merchantDetailsRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        String token = null;
        UserDto userDto = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userDto = (UserDto)userService.getLoginUser(token).getData();
            String name = userDto.getName();
            String email = userDto.getEmail();
            String role = userDto.getRole();
           // String ipAddress  = request.getRemoteAddr();
            String ipAddress = RemoteIpHelper.getRemoteIpFrom(request);

            MerchantDetails merchantDetails = getMerchantDetails(name,email,role,ipAddress);
            try {
                 merchantDetailsRepository.save(merchantDetails);
            }catch (Exception e) {
                log.error(e.getMessage());
            }

        }
       // return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public MerchantDetails getMerchantDetails(String name, String email, String role, String ipAddress) {
        MerchantDetails merchantDetails = new MerchantDetails();
        merchantDetails.setName(name);
        merchantDetails.setEmail(email);
        merchantDetails.setRole(role);
        merchantDetails.setIpAddress(ipAddress);
        merchantDetails.setCreatedAt(LocalDateTime.now());
        return merchantDetails;
    }
}
