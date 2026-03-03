package com.java6.springboot.lab1.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Kích hoạt @PreAuthorize cho Lab 3
public class SecurityConfig {

        private final DataSource dataSource;
        private final UserDetailsService userDetailsService;

        public SecurityConfig(DataSource dataSource, UserDetailsService userDetailsService) {
                this.dataSource = dataSource;
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        // Đã xóa cấu hình UserDetailsService bằng JDBC của Lab 2
        // Thay vào đó, chúng ta sẽ bắt DI cho tham số UserDetailsService trong
        // constructor

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) {
                // 1. Cấu hình OAuth2 Login (Lab 3)
                http.oauth2Login(oauth2 -> oauth2
                                .loginPage("/login")
                                .successHandler((request, response, authentication) -> {
                                        // Lấy thông tin từ Google
                                        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                                        assert oidcUser != null;
                                        String email = oidcUser.getEmail();
                                        String role = "OAUTH";

                                        // Tạo UserDetails ảo để Spring Security nhận diện quyền
                                        UserDetails newUser = User.withUsername(email)
                                                        .password("{noop}")
                                                        .roles(role)
                                                        .build();

                                        // Ghi đè đối tượng Authentication hiện tại
                                        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                                                        newUser, null, newUser.getAuthorities());
                                        SecurityContextHolder.getContext().setAuthentication(newAuth);

                                        // Chuyển hướng về trang yêu cầu trước đó hoặc trang chủ
                                        HttpSession session = request.getSession();
                                        DefaultSavedRequest savedRequest = (DefaultSavedRequest) session
                                                        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                                        String redirectUrl = (savedRequest == null) ? "/"
                                                        : savedRequest.getRedirectUrl();
                                        response.sendRedirect(redirectUrl);
                                })
                                .permitAll());

                // 2. Bỏ qua CSRF và CORS cho môi trường học tập
                http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);

                // 3. Phân quyền truy xuất (Lab 2)
                // Lưu ý: Nếu dùng @PreAuthorize ở Controller thì có thể để
                // .anyRequest().permitAll() ở đây
                // http.authorizeHttpRequests(auth -> auth
                // .requestMatchers("/poly/url1").authenticated()
                // .requestMatchers("/poly/url2").hasRole("USER")
                // .requestMatchers("/poly/url3").hasRole("ADMIN")
                // .requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN")
                // .anyRequest().permitAll()
                // );
                http.authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll());

                // 4. Xử lý từ chối truy cập
                http.exceptionHandling(ex -> ex.accessDeniedPage("/access/denied"));

                // 5. Cấu hình Form Login tùy biến
                http.formLogin(login -> login
                                .loginPage("/login")
                                .loginProcessingUrl("/login/check")
                                .defaultSuccessUrl("/login/success", true)
                                .failureUrl("/login/failure")
                                .permitAll());

                // 6. Ghi nhớ đăng nhập (Remember Me)
                http.rememberMe(rm -> rm
                                .tokenValiditySeconds(3 * 24 * 60 * 60) // 3 ngày
                                .rememberMeParameter("remember-me")
                                .userDetailsService(userDetailsService) // Sử dụng UserDetailsService được inject (từ
                                                                        // Lab 3)
                );

                // 7. Đăng xuất
                http.logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login/exit")
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .deleteCookies("remember-me"));

                return http.build();
        }
}