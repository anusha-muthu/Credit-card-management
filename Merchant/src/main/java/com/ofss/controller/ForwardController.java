package com.ofss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForwardController {

    @GetMapping("/register")
    public String registerPage() {

        return "forward:/register.html";
    }
    @GetMapping("/api/merchants/update")
    public String detailsPage() {

        return "forward:/merchant_details.html";
    }
    
}