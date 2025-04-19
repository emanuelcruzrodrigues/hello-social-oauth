package com.emanuel.hello.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("account", request.getSession().getAttribute("account"));
        model.addAttribute("login", request.getSession().getAttribute("login"));
        return "index";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        return "logout";
    }
}
