package com.shapesynergy.dietworkout.appuser;

import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


//All access-free endpoints are defined here
@Controller
@RequiredArgsConstructor
public class AppUserController {

    @Autowired
    private final AppUserService appUserService;

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "register.html";
    }
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") AppUserDTO appUserDTO, Model model) {
        appUserService.save(appUserDTO);
        model.addAttribute("message", "User registered successfully");
        return "login.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }
}
