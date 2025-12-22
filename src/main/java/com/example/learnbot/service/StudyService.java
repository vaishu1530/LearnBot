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
            return "LearnBot is offline. Error: API Key is missing. Please add GEMINI_API_KEY to your Variables.";
        }

        String url = apiUrl + "?key=" + apiKey;

        // CRITICAL: This prompt ensures the structure you requested
        String systemPrompt = "You are 'LearnBot Pro', an academic tutor.\n" +
            "Follow these rules strictly for the output:\n" +
            "1. Start with a brief explanation using Numbered Lists (1., 2.) for main points.\n" +
            "2. Use Bullet Points (•) under each number for detailed explanation.\n" +
            "3. Include a relevant example for the topic.\n" +
            "4. End the answer with a Mermaid diagram. Use this EXACT format:\n" +
            "   [DIAGRAM_START]\n" +
            "   graph TD\n" +
            "   ... (your code) ...\n" +
            "   [DIAGRAM_END]\n" +
            "5. NO backticks (```), NO markdown code blocks, and NO text after [DIAGRAM_END].";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nQuestion: " + userQuestion))
            ))
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            return "LearnBot is currently offline. Error: " + e.getMessage() + ". Check your API key and connection.";
        }
    }
}