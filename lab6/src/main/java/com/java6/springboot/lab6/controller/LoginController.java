package com.java6.springboot.lab6.controller;

import com.java6.springboot.lab6.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    final
    AuthenticationManager authenticationManager;
    final
    JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/poly/login")
    public Object login(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        var authInfo = new UsernamePasswordAuthenticationToken(username,
                password);
        var authentication = authenticationManager.authenticate(authInfo);
        if (authentication.isAuthenticated()) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            assert user != null;
            String token = jwtService.create(user, 20 * 60); // token có hiệu lực 20 phut

            return Map.of("token", token);
        }
        throw new UsernameNotFoundException("Username not found!");
    }
}

