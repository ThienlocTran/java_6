package com.java6.springboot.lab1.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service("auth")
public class AuthService {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUsername() {
        return getAuthentication().getName();
    }

    public List<String> getRoles() {
        return getAuthentication().getAuthorities().stream()
                .map(au -> Objects.requireNonNull(au.getAuthority()).replace("ROLE_", ""))
                .toList();
    }

    public boolean isAuthenticated() {
        String username = getUsername();
        return username != null && !username.equals("anonymousUser");
    }
    public boolean hasAnyRole(String... rolesToCheck) {
        List<String> grantedRoles = this.getRoles();
        // Kiểm tra xem user có bất kỳ role nào trong danh sách truyền vào không [cite: 151]
        return Stream.of(rolesToCheck).anyMatch(grantedRoles::contains);
    }
}
