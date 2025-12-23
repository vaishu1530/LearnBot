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
        // 1. Identify the logged-in student
        User user = (User) session.getAttribute("loggedInUser");
        
        // 2. Get the AI Response
        String aiResponse = studyService.getStudyLogic(question);

        // 3. FEATURE: SAVE CHAT HISTORY
        if (user != null) {
            ChatHistory chat = new ChatHistory();
            chat.setQuestion(question);
            chat.setResponse(aiResponse);
            chat.setUser(user); // Connects history to this student's ID
            chatRepository.save(chat); // Saves to the chat_history table
        }

        // 4. FEATURE: PROVIDE HISTORY TO THE UI
        Long userId = (user != null) ? user.getId() : 1L;
        List<ChatHistory> history = chatRepository.findByUserIdOrderByIdDesc(userId);
        
        model.addAttribute("question", question);
        model.addAttribute("botResponse", aiResponse);
        model.addAttribute("history", history); // Sends history list to Thymeleaf
        
        return "ask"; 
    }
}