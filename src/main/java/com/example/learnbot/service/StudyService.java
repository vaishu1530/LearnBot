package com.example.learnbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class StudyService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getStudyLogic(String userQuestion) {
        // 1. Safety Check: If key is missing, don't crash, just tell the user.
        if (apiKey == null || apiKey.isEmpty()) {
            return "LearnBot is offline: Please add your GEMINI_API_KEY to Zeabur Variables.";
        }

        // 2. Build the URL correctly
        String url = apiUrl + "?key=" + apiKey;

        // 3. System Prompt for Diagrams
        String systemPrompt = "You are 'LearnBot Pro'. Answer only academic questions.\n" +
            "1. Use (1., 2.) for headings and (•) for bullets.\n" +
            "2. If a topic is visual, you MUST provide a Mermaid diagram starting with 'graph TD'.\n" +
            "3. Put technical terms in parentheses ().";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nQuestion: " + userQuestion))
            ))
        );

        try {
            System.out.println("Connecting to Gemini...");
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            System.err.println("GEMINI ERROR: " + e.getMessage());
            return "LearnBot is currently offline. Error: " + e.getMessage();
        }
    }
}