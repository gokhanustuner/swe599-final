package com.swe599final.mdm.application.controller;

import com.swe599final.mdm.domain.service.UserRegistrationService;
import com.swe599final.mdm.infrastructure.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
final public class RegistrationFormController {

    @Autowired
    UserRegistrationService userRegistrationService;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "registration_form";
    }

    @PostMapping("/register_user")
    public String postRegistrationForm(User user) {
        userRegistrationService.createUser(user);

        return "register_success";
    }
}
