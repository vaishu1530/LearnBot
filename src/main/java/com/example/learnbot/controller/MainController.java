package com.example.learnbot.controller;

import com.example.learnbot.model.User;
import com.example.learnbot.service.UserService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public String doLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(email, password); 
        
        if (user != null) {
            // MUST be "loggedInUser"
            session.setAttribute("loggedInUser", user); 
            return "redirect:/ask"; // This sends the browser to the Bot page
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
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
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}