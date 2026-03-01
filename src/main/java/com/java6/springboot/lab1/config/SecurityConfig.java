package com.java6.springboot.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Thêm cái này để kích hoạt tính năng bảo mật web
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        String password = pe.encode("123");
        UserDetails user1 = User.withUsername("user@gmail.com").password(password).roles().build();
        UserDetails user2 = User.withUsername("admin@gmail.com").password(password).roles().build();
        UserDetails user3 = User.withUsername("both@gmail.com").password(password).roles().build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
// Bỏ cấu hình mặc định CSRF và CORS
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
// Phân quyền sử dụng
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/poly/**").authenticated() // Bảo vệ các URL bắt đầu bằng /poly/
                        .anyRequest().permitAll()); // Các URL khác (như /) cho phép truy cập tự do
// Form đăng nhập mặc định
        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
// Form đăng nhập
        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
// Ghi nhớ tài khoản
        http.rememberMe(config -> config.tokenValiditySeconds(3 * 24 * 60 * 60));
// Đăng xuất
        http.logout(Customizer.withDefaults());
        return http.build();
    }

}
