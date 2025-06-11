package com.emanuel.hello.controller;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public String login(Model model, HttpServletRequest request) {
        Account account = accountService.getAccount((String) request.getSession().getAttribute("accountId"));
        model.addAttribute("account", account);
        return "account";
    }
}
