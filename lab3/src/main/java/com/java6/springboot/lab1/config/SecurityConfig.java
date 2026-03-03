package com.java6.springboot.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity // Thêm cái này để kích hoạt tính năng bảo mật web
public class SecurityConfig {


    final UserDetailsService userDetailsService; // Inject bean mà bạn đã định nghĩa (Dao hoặc Jdbc)
    final
    DataSource dataSource;


    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDetailsService = new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.oauth2Login(login -> {
            login.permitAll();
        });
// Bỏ cấu hình mặc định CSRF và CORS
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
// Phân quyền sử dụng
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/poly/url1").authenticated() // Bảo vệ các URL bắt đầu bằng /poly/
                .requestMatchers("/poly/url2").hasRole("USER")
                .requestMatchers("/poly/url3").hasRole("ADMIN")
                .requestMatchers("/poly/url4").hasAnyRole("USER","ADMIN")
                        .anyRequest().permitAll()); // Các URL khác (như /) cho phép truy cập tự do

        // Khi bị từ chối sẽ dẫn về cái này
        http.exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));
// Form đăng nhập mặc định
        // 2. Tùy biến Form Login
        http.formLogin(login -> login
                .loginPage("/login/form")                // Link hiển thị form
                .loginProcessingUrl("/login/check")      // Link xử lý login (khớp với form action) [cite: 216]
                .defaultSuccessUrl("/login/success", true) // Thành công thì đi đâu
                .failureUrl("/login/failure")            // Thất bại thì đi đâu
                .usernameParameter("username")           // Tên input username
                .passwordParameter("password")           // Tên input password
                .permitAll()
        );

// Ghi nhớ tài khoản
        http.rememberMe(rm -> rm
                .tokenValiditySeconds(3 * 24 * 60 * 60) // 3 ngày
                .rememberMeParameter("remember-me")     // Tên checkbox
                .userDetailsService(userDetailsService)
        );
// Dang suat
        http.logout(config -> {
            config.logoutUrl("/logout");                // Đường dẫn kích hoạt đăng xuất [cite: 231]
            config.logoutSuccessUrl("/login/exit");     // Thoát xong thì nhảy về LoginController với action 'exit'
            config.clearAuthentication(true);           // Xóa thông tin xác thực
            config.invalidateHttpSession(true);         // Hủy Session hiện tại
            config.deleteCookies("remember-me");        // Xóa Cookie ghi nhớ
        });
        return http.build();
    }

}
