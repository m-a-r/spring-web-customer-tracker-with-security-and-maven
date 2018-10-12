package com.luv2code.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("showLoginPage")
    public String showLoginPage() {
        return "fancy-login";
    }

    @GetMapping("access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
