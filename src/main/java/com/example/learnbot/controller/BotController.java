// src/main/java/com/example/learnbot/controller/BotController.java
package com.example.learnbot.controller;

import com.example.learnbot.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BotController {

    @Autowired
    private StudyService studyService;

    // 1. Redirect home to /ask
    @GetMapping("/")
    public String index() {
        return "redirect:/ask";
    }

    // 2. SHOW the ask.html page
    @GetMapping("/ask")
    public String showAskPage() {
        return "ask"; 
    }

    // 3. HANDLE the AI logic (the chat button)
    @PostMapping("/ask")
    public String processQuestion(@RequestParam String question, Model model) {
        try {
            String response = studyService.getStudyLogic(question);
            model.addAttribute("botResponse", response);
        } catch (Exception e) {
            model.addAttribute("botResponse", "LearnBot is currently offline. Error: " + e.getMessage());
        }
        return "ask"; // Ensure this matches your HTML file name exactly
    }
}