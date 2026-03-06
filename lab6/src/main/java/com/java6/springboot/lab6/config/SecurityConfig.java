package com.java6.springboot.lab6.config;

import com.java6.springboot.lab6.service.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        UserDetails user1 =
                User.withUsername("user@gmail.com").password(pe.encode("123")).roles("USER").build();
        UserDetails user2 =
                User.withUsername("admin@gmail.com").password(pe.encode("123")).roles("ADMIN").build
                        ();
        UserDetails user3 =
                User.withUsername("both@gmail.com").password(pe.encode("123")).roles("USER",
                        "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter
            jwtAuthFilter) {
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(config -> {
            config.requestMatchers("/poly/url1").authenticated();
            config.requestMatchers("/poly/url2").hasRole("USER");
            config.requestMatchers("/poly/url3").hasRole("ADMIN");
            config.requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN");
            config.anyRequest().permitAll();
        });
// Bỏ duy trì user trong session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                                    config) {
        return config.getAuthenticationManager();
    }
}

