package com.java6.springboot.lab1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class MyController {

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "@/ => home()");
        return "page";
    }
    @RequestMapping("/poly/url0")
    public String method0(Model model) {
        model.addAttribute("message", "@/poly/url0 => method1()");
        return "page";
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/poly/url1")
    public String method1(Model model) {
        model.addAttribute("message", "@/poly/url1 => method1()");
        return "page";
    }
    @PreAuthorize("hasRole('USER')") @RequestMapping("/poly/url2")
    public String method2(Model model) {
        model.addAttribute("message", "@/poly/url2 => method2()");
        return "page";
    }

    @PreAuthorize("hasRole('ADMIN')") @RequestMapping("/poly/url3")
    public String method3(Model model) {
        model.addAttribute("message", "@/poly/url3 => method3()");
        return "page";
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") @RequestMapping("/poly/url4")
    public String method4(Model model) {
        model.addAttribute("message", "@/poly/url4 => method4()");
        return "page";
    }

}
