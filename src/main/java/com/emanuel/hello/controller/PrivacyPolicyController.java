package com.emanuel.hello.controller;

import com.emanuel.hello.domain.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyPolicyController {
    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy() {
        return "privacy_policy";
    }
    @GetMapping("/facebook-data-deletion-instructions")
    public String getFacebookDataDeletionInstructions() {
        return "facebook_data_deletion_instructions";
    }
}
