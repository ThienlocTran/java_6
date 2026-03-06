package com.java6.springboot.lab6.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@CrossOrigin("*")
@RestController
public class MyRestApi {
    @GetMapping({"/poly/url0", "/"})
    public Object method0() {
        return Map.of("url", "/poly/url0", "method", "method0()");
    }
    @GetMapping("/poly/url1")
    public Object method1() {
        return Map.of("url", "/poly/url1", "method", "method1()");
    }
    @GetMapping("/poly/url2")
    public Object method2() {
        return Map.of("url", "/poly/url2", "method", "method2()");
    }
    @GetMapping("/poly/url3")
    public Object method3() {
        return Map.of("url", "/poly/url3", "method", "method3()");
    }
    @GetMapping("/poly/url4")
    public Object method4() {
        return Map.of("url", "/poly/url4", "method", "method4()");
    }

    }

