package com.example.learnbot.controller;

import com.example.learnbot.model.User;
import com.example.learnbot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    

   

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Spring Security handles POST /login automatically
    // No need to manually handle login

    // ====== REGISTER ======
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(User user, Model model) {

        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        userService.save(user);
        model.addAttribute("success", "Account created successfully!");
        return "login";
    }

    // ====== HOME ======
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

   
}
