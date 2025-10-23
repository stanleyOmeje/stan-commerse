package com.stan.stancommerce.service.security.filter;

import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final View error;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //check if header is passed
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //extract token
        String token = authHeader.replace("Bearer ", "");
        //validate Token
        if (!jwtService.validatoken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //extract email
        UserDetails userDetails = null;
        String id = jwtService.getSubject(token);
        Optional<User> user = userRepository.findById(Long.parseLong(id));
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        String email = user.get().getEmail();
        Authentication uAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isNotBlank(email) && uAuthentication != null) {
            filterChain.doFilter(request, response);
            return;
        }
        userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());
        //call for Username password authentication Token
        var authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        // set the athentication object with additonal details
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // set it to security context holder for future use
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // call the next filter
        filterChain.doFilter(request, response);
        // in configuration, set this filter as the first filter

    }
}
