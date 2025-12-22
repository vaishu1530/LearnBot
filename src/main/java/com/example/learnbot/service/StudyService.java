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
            return "LearnBot is offline. Error: API key missing in environment variables.";
        }

        String url = apiUrl + "?key=" + apiKey;

        // Prompt designed for "Perplexity" style split (Text + Diagram)
        String systemPrompt = "You are 'LearnBot Pro'.\n" +
            "1. Give a brief academic explanation using numbered lists (1. 2.).\n" +
            "2. End with exactly ONE diagram.\n" +
            "3. Format the diagram as: [DIAGRAM_START] graph TD ... [DIAGRAM_END]\n" +
            "4. DO NOT use backticks (```) or the word 'mermaid' anywhere.";

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
            return "LearnBot is currently offline. Error: " + e.getMessage() + ". Check your database connection and API key.";
        }
    }
}