package com.java6.springboot.lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class LoginController {
    @RequestMapping("/login") // Khi gõ /login
    public String loginForm(Model model) {
        model.addAttribute("message", "Vui lòng đăng nhập");
        return "login";
    }
    @RequestMapping("/login/{action}")
    public String login(Model model, @PathVariable("action") String action) {
        switch(action) {
            case "form" -> model.addAttribute("message", "Vui lòng đăng nhập");
            case "success" -> model.addAttribute("message", "Đăng nhập thành công");
            case "failure" -> model.addAttribute("message", "Sai thông tin đăng nhập");
            case "exit" -> model.addAttribute("message", "Đăng xuất thành công");
            default -> model.addAttribute("message","Bạn đang login" );
        }
        return "login"; // Trả về file login.html
    }
}
