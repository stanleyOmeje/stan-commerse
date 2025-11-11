package com.stan.stancommerce.config.securityconfig;


import com.stan.stancommerce.enums.Roles;
import com.stan.stancommerce.service.security.UserDService;
import com.stan.stancommerce.service.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //stripe secretKey

    //sk_test_51SPKHsRNfhAKWHzBEkxxNi1vt6JGTmc7rVbrvXlFoa7gBTnYMGyKe8LBRupnWZ1kOHY8tbb1EuBCDAl6H8WEGq2v00o8Peas00

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement(c->
        {
            try {
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .disable().csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a->
                        a.requestMatchers("/carts/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"users").permitAll()
                            .requestMatchers("users/login").permitAll()
                            .requestMatchers("users/**").hasAuthority(Roles.USER.name())
                            .requestMatchers("auth/login").permitAll()
                            .requestMatchers("auth/login").permitAll()
                            .requestMatchers("checkout/webhook").permitAll()
                            .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return http.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
       authProvider.setPasswordEncoder(passwordEncoder());
       return authProvider;
    }
}
