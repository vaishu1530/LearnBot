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
        if (apiKey == null || apiKey.isEmpty()) {
            return "LearnBot is offline: Missing API Key.";
        }

        // Join URL and Key
        String url = apiUrl + "?key=" + apiKey;

        String systemPrompt = "You are 'LearnBot Pro'.\n" +
            "1. Use (1., 2.) for headings.\n" +
            "2. Include Mermaid diagrams using 'graph TD' for visual topics.\n" +
            "3. Use (technical terms) in parentheses.";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nQuestion: " + userQuestion))
            ))
        );

        try {
            // Attempt the call to the NEW Gemini 3 endpoint
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            // This will now tell you if it's still a 404 or something else
            System.err.println("API ERROR: " + e.getMessage());
            return "LearnBot is currently offline. Error: " + e.getMessage();
        }
    }
}
    