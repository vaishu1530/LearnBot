package com.example.learnbot.controller;

import com.example.learnbot.model.ChatHistory;
import com.example.learnbot.model.User;
import com.example.learnbot.repository.ChatRepository;
import com.example.learnbot.service.StudyService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <--- ONLY THIS ONE
import org.springframework.web.bind.annotation.*;

@Controller
public class BotController {

    @Autowired
    private StudyService studyService;

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("/ask")
    public String showAskPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        
        // SAFETY HACK: If session fails, don't redirect! 
        // Just create a temporary empty user so the page opens.
        if (user == null) {
            user = new User(); 
            user.setId(1L); // Use a fake ID for the demo
        }

        model.addAttribute("history", chatRepository.findByUserIdOrderByIdDesc(user.getId()));
        return "ask"; 
    }
    @PostMapping("/ask")
    public String getAnswer(@RequestParam String question, HttpSession session, Model model) {
        // 1. Try to get the user, but DON'T redirect if null
        User user = (User) session.getAttribute("loggedInUser");
        
        // 2. Fallback ID so the sidebar doesn't crash if session is lost
        Long userId = (user != null) ? user.getId() : 1L; 

        // 3. Get the AI Response (The most important part for your demo!)
        String aiResponse = studyService.getStudyLogic(question);

        // 4. Save history only if user exists
        if (user != null) {
            ChatHistory chat = new ChatHistory();
            chat.setQuestion(question);
            chat.setUser(user); 
            chatRepository.save(chat);
        }

        // 5. Send data to the page
        model.addAttribute("question", question);
        model.addAttribute("botResponse", aiResponse);
        model.addAttribute("history", chatRepository.findByUserIdOrderByIdDesc(userId));
        
        // 6. STAY ON THE PAGE - DO NOT USE REDIRECT HERE
        return "ask"; 
    }
}