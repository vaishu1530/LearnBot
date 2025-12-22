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
        // 1. Validation: Prevent API calls if the key is missing
        if (apiKey == null || apiKey.isEmpty()) {
            return "LearnBot is offline: Missing API Key. Please set GEMINI_API_KEY in your environment.";
        }

        // 2. Final Endpoint URL
        String url = apiUrl + "?key=" + apiKey;

        // 3. The "Final Exam" System Prompt
        String systemPrompt = "You are 'LearnBot Pro'. Follow these rules strictly:\n" +
            "1. Use Numbered Lists (1., 2.) for main steps.\n" +
            "2. Use Bullet Points (•) for sub-details.\n" +
            "3. If a concept is visual, you MUST draw a Mermaid diagram using 'graph TD' syntax.\n" +
            "4. Keep technical terms in parentheses (Example).";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nQuestion: " + userQuestion))
            ))
        );

        try {
            // Send request to Gemini 3 Flash
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            
            // Extract the text content safely
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            // Print actual error to logs for debugging
            System.err.println("API CRITICAL ERROR: " + e.getMessage());
            return "LearnBot is currently offline. Error Detail: " + e.getMessage();
        }
    }
}